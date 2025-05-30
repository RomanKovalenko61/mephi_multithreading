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

    public Worker(BlockingQueue<Runnable> queue, long keepAliveTime, TimeUnit keepAliveTimeUnit) {
        this.queue = queue;
        this.isTerminated = false;
        this.keepAliveTime = keepAliveTime;
        this.keepAliveTimeUnit = keepAliveTimeUnit;
    }

    @Override
    public void run() {
        while (!isTerminated) {
            try {
                Runnable task = queue.poll(keepAliveTime, keepAliveTimeUnit);
                if (task != null) {
                    LOG.info("[Worker] " + Thread.currentThread().getName() + " начинает выполнение задачи.");
                    task.run();
                    LOG.info("[Worker] " + Thread.currentThread().getName() + " завершил выполнение задачи.");
                } else {
                    LOG.info("[Worker] " + Thread.currentThread().getName() + " время ожидания истекло, завершение работы.");
                    break;
                }
            } catch (InterruptedException e) {
                isTerminated = true;
            }
        }
    }

    void terminate() {
        isTerminated = true;
    }
}
