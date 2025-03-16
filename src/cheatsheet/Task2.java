package cheatsheet;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 42, executor);
        executor.shutdown();
        System.out.println(future.get()); // return 42
    }
}
