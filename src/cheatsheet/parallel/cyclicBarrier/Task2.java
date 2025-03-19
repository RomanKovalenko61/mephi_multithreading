package cheatsheet.parallel.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task2 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("Все потоки завершили сбор данных. Начинаем анализ...");
        });
        Runnable task1 = () -> {
            System.out.println("Задача 1: Получение прогноза погоды из источника 1...");
            try {
                Thread.sleep(1000);
                barrier.await();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Задача 1: Готово");
        };
        Runnable task2 = () -> {
            System.out.println("Задача 2: Получение прогноза погоды из источника 2...");
            try {
                Thread.sleep(3000);
                barrier.await();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Задача 2: Готово");
        };
        Runnable task3 = () -> {
            System.out.println("Задача 3: Получение прогноза погоды из источника 3...");
            try {
                Thread.sleep(1200);
                barrier.await();
            } catch (Exception e) {
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
