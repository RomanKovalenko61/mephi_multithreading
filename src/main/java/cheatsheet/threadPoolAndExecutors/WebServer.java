package cheatsheet.threadPoolAndExecutors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WebServer {
    public static void main(String[] args) {
        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 30;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " is processing a request");
                // Имитация обработки запроса
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        executor.shutdown();
    }
}
