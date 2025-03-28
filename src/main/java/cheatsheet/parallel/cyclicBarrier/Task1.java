package cheatsheet.parallel.cyclicBarrier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task1 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Runnable task1 = () -> {
            System.out.println("Задача 1: Получение прогноза погоды из источника 1...");
            try {
                Thread.sleep(1000); // Имитация времени на выполнение
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Задача 1: Готово");
        };
        Runnable task2 = () -> {
            System.out.println("Задача 2: Получение прогноза погоды из источника 2...");
            try {
                Thread.sleep(3000); // Имитация времени на выполнение
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Задача 2: Готово");
        };
        Runnable task3 = () -> {
            System.out.println("Задача 3: Получение прогноза погоды из источника 3...");
            try {
                Thread.sleep(1200); // Имитация времени на выполнение
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Задача 3: Готово");
        };
        executor.submit(task1);
        executor.submit(task2);
        executor.submit(task3);
        executor.shutdown();
    }
}
