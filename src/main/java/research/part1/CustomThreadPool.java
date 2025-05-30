package research.part1;

import java.util.Queue;
import java.util.concurrent.*;

public class CustomThreadPool implements CustomExecutor {
    private int corePoolSize;
    private int maxPoolSize;
    private int keepAliveTime;
    private TimeUnit timeUnit;
    private int queueSize;
    private int minSpareThreads;

    private ThreadFactory threadFactory;
    private Queue<Worker> workers;
    private BlockingQueue<Runnable> workQueue;

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
        initializeWorkers(minSpareThreads);
    }

    private void createWorker() {
        if (workers.size() < maxPoolSize) {
            Worker worker = new Worker(workQueue, keepAliveTime, timeUnit);
            workers.offer(worker);
            threadFactory.newThread(worker).start();
        }
    }

    private void initializeWorkers(int minSpareThreads) {
        for (int i = 0; i < minSpareThreads; i++) {
            createWorker();
        }
    }

    @Override
    public void execute(Runnable command) {
        System.out.println("[Pool] Добавление задачи в очередь");
        workQueue.add(command);
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return null;
    }

    @Override
    public void shutdown() {
        workers.forEach(Worker::terminate);
    }

    @Override
    public void shutdownNow() {

    }
}
