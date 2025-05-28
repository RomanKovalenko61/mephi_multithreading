package cheatsheet.ThreadPoolAndExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            Runnable task = new WorkerThread("Task " + i);
            executor.execute(task);
        }
        executor.shutdown();
    }
}
