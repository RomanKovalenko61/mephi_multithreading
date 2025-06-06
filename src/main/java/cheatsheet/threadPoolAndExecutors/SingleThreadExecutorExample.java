package cheatsheet.threadPoolAndExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutorExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            Runnable task = new WorkerThread("Task " + i);
            executor.execute(task);
        }
        executor.shutdown();
    }
}
