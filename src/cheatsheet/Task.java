package cheatsheet;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Task {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            System.out.println("Асинхронная задача выполняется...");
        });

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 42);
        CompletableFuture<String> str = future.thenApply(answer -> "Результат " + answer);
        System.out.println(str.get());

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 42);
        future1.thenAccept(result -> {
            System.out.println("Полученное значение: " + result);
        });

        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> 42);
        future.thenRun(() -> {
            System.out.println("Задача завершена, но результат нам не нужен.");
        });

        System.out.println(future3.get()); // return 42

        CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> 20);
        CompletableFuture<Integer> future5 = future4.thenCompose(result -> CompletableFuture.supplyAsync(() -> result * 2));
        System.out.println(future5.join()); // Результат: 40

        CompletableFuture<Integer> future6 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> future7 = CompletableFuture.supplyAsync(() -> 20);
        CompletableFuture<Integer> combinedFuture = future6.thenCombine(future7, Integer::sum); //(a, b) -> Integer.sum(a, b)
        System.out.println(combinedFuture.join()); // Результат: 30
    }
}

