package cheatsheet.ThreadPoolAndExecutors;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShutDownNowEx {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            Runnable task = new WorkerThread("Task " + i);
            executor.execute(task);
        }
        List<Runnable> notExecuted = executor.shutdownNow(); // Прерывает выполнение и возвращает невыполненные задачи
        System.out.println("Невыполненных задач: " + notExecuted.size());
    }
}
