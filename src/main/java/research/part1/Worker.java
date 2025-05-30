package research.part1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Worker implements Runnable {
    private static final Logger LOG = Logger.getLogger(Worker.class.getName());

    private final BlockingQueue<Runnable> queue;
    private volatile boolean isTerminated;
    private final long keepAliveTime;
    private final TimeUnit keepAliveTimeUnit;

    private final CustomThreadPool pool;

    public Worker(CustomThreadPool pool, BlockingQueue<Runnable> queue, long keepAliveTime, TimeUnit keepAliveTimeUnit) {
        this.pool = pool;
        this.queue = queue;
        this.isTerminated = false;
        this.keepAliveTime = keepAliveTime;
        this.keepAliveTimeUnit = keepAliveTimeUnit;
    }

    @Override
    public void run() {
        while (!isTerminated) {
            pool.onWorkerIdle();
            Runnable task = null;
            try {
                task = queue.poll(keepAliveTime, keepAliveTimeUnit);
                if (Thread.currentThread().isInterrupted()) {
                    LOG.warning("[Worker] " + Thread.currentThread().getName() + " обнаружил прерывание.");
                    break;
                }
                if (task != null) {
                    if (pool.isShutdown().get()) {
                        LOG.info("[Worker] " + Thread.currentThread().getName() + " завершает работу из-за shutdown.");
                        break;
                    }
                    pool.onWorkerBusy();
                    LOG.info("[Worker] " + Thread.currentThread().getName() + " начинает выполнение задачи.");
                    task.run();
                    LOG.info("[Worker] " + Thread.currentThread().getName() + " завершил выполнение задачи.");
                } else {
                    if (pool.getWorkerCount() > pool.getMinSpareThreads()) {
                        LOG.info("[Worker] " + Thread.currentThread().getName() + " ожидал задачи, но не получил их. Будет остановлен");
                        break;
                    }
                }
            } catch (InterruptedException e) {
                LOG.warning("[Worker] " + Thread.currentThread().getName() + " был прерван: " + e.getMessage());
            } finally {
                if (task != null) {
                    LOG.info("[Worker] " + Thread.currentThread().getName() + " завершает выполнение задачи");
                }
            }
        }
        pool.onWorkerExit(this);
    }

    void terminate() {
        isTerminated = true;
    }
}
