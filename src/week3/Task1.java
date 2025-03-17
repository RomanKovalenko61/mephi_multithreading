package week3;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task1 {
    static Double a, b, c, d;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Расчет по формуле (a ^ 2 + b ^ 2) * log(c) / sqrt(d)");
        System.out.println("Введите значение a");
        a = scanner.nextDouble();
        System.out.println("Введите значение b");
        b = scanner.nextDouble();
        System.out.println("Введите значение c");
        c = scanner.nextDouble();
        System.out.println("Введите значение d");
        d = scanner.nextDouble();

        System.out.println("Вычисляю результат...");

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CompletableFuture<Double> sumOfSquares = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return a * a + b * b;
        }, executorService);
        CompletableFuture<Double> logC = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Math.log(c);
        }, executorService);
        CompletableFuture<Double> sqrtD = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Math.sqrt(d);
        }, executorService);

        executorService.shutdown();

        System.out.println("Calculating sum of squares: " + sumOfSquares.join());
        System.out.println("Calculating sqrt(d): " + sqrtD.join());
        System.out.println("Calculating log(c): " + logC.join());
        System.out.println("Final result of the formula: " + sumOfSquares.join() * logC.join() / sqrtD.join());
    }
}
