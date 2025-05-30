package research.part1;

import java.util.Queue;
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
    private Queue<Worker> workers;
    private BlockingQueue<Runnable> workQueue;
    private AtomicBoolean isShutdown = new AtomicBoolean(false);
    private AtomicInteger countWorkers = new AtomicInteger(0);
    private AtomicInteger idleWorkers = new AtomicInteger(0);
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
        workers = new ArrayBlockingQueue<>(maxPoolSize);
        workQueue = new LinkedBlockingQueue<>(queueSize);
        initializeWorkers();
    }

    public void setRejectPolicy(RejectPolicy rejectPolicy) {
        this.rejectPolicy = rejectPolicy;
    }

    private void createWorker() {
        if (workers.size() < maxPoolSize) {
            Worker worker = new Worker(this, workQueue, keepAliveTime, timeUnit);
            workers.offer(worker);
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

    protected void taskCompleted() {
        countWorkers.decrementAndGet();
    }

    protected void onWorkerExit(Worker worker) {
        workers.remove(worker);
    }

    @Override
    public synchronized void execute(Runnable command) {
        if (isShutdown.get()) {
            LOG.warning("[Pool] Попытка выполнить задачу после остановки пула потоков.");
            //throw new RejectedExecutionException("Пул потоков остановлен, добавление новой задачи невозможно.");
        }
        if (workQueue.size() >= queueSize) {
            LOG.warning("[Pool] Очередь задач переполнена. Применение политики отклонения.");
            rejectPolicy.handle(command);
        }
        LOG.info("[Pool] Добавление задачи в очередь");
        workQueue.offer(command);
        if (!workQueue.isEmpty()) {
            createWorker();
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return null;
    }

    @Override
    public void shutdown() {
        isShutdown.set(true);
        workers.forEach(Worker::terminate);
        LOG.info("[Pool] Завершение работы пула потоков.");
        workers.clear();
    }

    @Override
    public void shutdownNow() {

    }
}
