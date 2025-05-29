package cheatsheet.threadPoolAndExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AwaitTerminationEx {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            executor.execute(new WorkerThread("Task " + i));
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Некоторые задачи не завершились в течение 5 секунд.");
            } else {
                System.out.println("Все задачи завершены.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
