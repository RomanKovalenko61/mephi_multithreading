package cheatsheet.threadPoolAndExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShutdownEx {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executor.execute(new WorkerThread("Task " + i));
        }
        executor.shutdown();
    }
}
