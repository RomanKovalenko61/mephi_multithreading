package research.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class CustomThreadPool implements CustomExecutor {
    private static final Logger LOG = Logger.getLogger(CustomThreadPool.class.getName());

    private int corePoolSize;
    private int maxPoolSize;
    private int keepAliveTime;
    private TimeUnit timeUnit;
    private int queueSize;
    private int minSpareThreads;

    private ThreadFactory threadFactory;
    private final List<Worker> workers = new ArrayList<>();
    private final List<BlockingQueue<Runnable>> workerQueues = new ArrayList<>();
    private AtomicBoolean isShutdown = new AtomicBoolean(false);
    private AtomicInteger countWorkers = new AtomicInteger(0);
    private AtomicInteger idleWorkers = new AtomicInteger(0);
    private final AtomicInteger RoundRobinIndex = new AtomicInteger(0);
    private RejectPolicy rejectPolicy = new AbortPolicy();

    public CustomThreadPool(int corePoolSize, int maxPoolSize, int keepAliveTime, TimeUnit timeUnit,
                            int queueSize, int minSpareThreads) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.queueSize = queueSize;
        this.minSpareThreads = minSpareThreads;
        this.threadFactory = new ThreadFactoryImpl();
        initializeWorkers();
    }

    public void setRejectPolicy(RejectPolicy rejectPolicy) {
        this.rejectPolicy = rejectPolicy;
    }

    private synchronized void createWorker() {
        if (workers.size() < maxPoolSize) {
            BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(queueSize);
            Worker worker = new Worker(this, queue, keepAliveTime, timeUnit);
            workers.add(worker);
            workerQueues.add(queue);
            threadFactory.newThread(worker).start();
            countWorkers.incrementAndGet();
        }
    }

    private void initializeWorkers() {
        for (int i = 0; i < corePoolSize; i++) {
            createWorker();
        }
    }

    protected void onWorkerIdle() {
        int idle = idleWorkers.incrementAndGet();
        if (idle < minSpareThreads && countWorkers.get() < maxPoolSize) {
            createWorker();
        }
    }

    protected void onWorkerBusy() {
        idleWorkers.decrementAndGet();
    }

    protected synchronized void onWorkerExit(Worker worker) {
        int idx = workers.indexOf(worker);
        if (idx >= 0) {
            workers.remove(idx);
            workerQueues.remove(idx);
            countWorkers.decrementAndGet();
        }
    }

    @Override
    public synchronized void execute(Runnable command) {
        if (isShutdown.get()) {
            LOG.warning("[Pool] Попытка выполнить задачу после остановки пула потоков.");
            throw new RejectedExecutionException("Пул потоков остановлен, добавление новой задачи невозможно.");
        }
        if (workers.isEmpty()) {
            createWorker();
        }
        int idx = RoundRobinIndex.getAndIncrement() % workerQueues.size();
        BlockingQueue<Runnable> queue = workerQueues.get(idx);
        if (!queue.offer(command)) {
            LOG.warning("[Pool] Очередь задач #" + idx + " переполнена. Применение политики отклонения.");
            rejectPolicy.handle(command);
        } else {
            LOG.info("[Pool] Добавление задачи в очередь #" + idx);
        }
        if (countWorkers.get() < maxPoolSize && !queue.isEmpty()) {
            createWorker();
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        if (isShutdown.get()) {
            throw new RejectedExecutionException("Пул потоков остановлен, добавление новой задачи невозможно.");
        }
        FutureTask<T> task = new FutureTask<>(callable);
        execute(task);
        return task;
    }

    @Override
    public void shutdown() {
        isShutdown.set(true);
        workers.forEach(Worker::terminate);
        LOG.info("[Pool] Завершение работы пула потоков.");
        workers.clear();
        workerQueues.clear();
    }

    @Override
    public void shutdownNow() {
        isShutdown.set(true);
        LOG.info("[Pool] Принудительное завершение пула потоков.");
        workers.forEach(Worker::terminate);
        for (BlockingQueue<Runnable> queue : workerQueues) {
            queue.clear();
        }
        workers.clear();
        workerQueues.clear();
        LOG.info("[Pool] Принудительное завершение пула потоков завершено.");
    }

    public int getWorkerCount() {
        return workers.size();
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public AtomicBoolean isShutdown() {
        return isShutdown;
    }
}
