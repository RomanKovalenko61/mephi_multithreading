package research.part1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Worker implements Runnable {
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
                    System.out.println("[Worker] " + Thread.currentThread().getName() + " начинает выполнение задачи.");
                    task.run();
                    System.out.println("[Worker] " + Thread.currentThread().getName() + " завершил выполнение задачи.");
                } else {
                    System.out.println("[Worker] " + Thread.currentThread().getName() + " время ожидания истекло, завершение работы.");
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
