package cheatsheet.ThreadPoolAndExecutors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        Runnable task1 = () -> System.out.println("Task 1 executed at " + System.currentTimeMillis());
        Runnable task2 = () -> System.out.println("Task 2 executed at " + System.currentTimeMillis());
        scheduler.schedule(task1, 5, TimeUnit.SECONDS); // Выполнить через 5 секунд
        scheduler.scheduleAtFixedRate(task2, 0, 3, TimeUnit.SECONDS); // Выполнять каждые 3 секунды
    }
}
