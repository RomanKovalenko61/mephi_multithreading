package cheatsheet.callableFuture;

import java.util.concurrent.*;

public class ExceptionHandlingExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Integer> callable = () -> {
            throw new Exception("Ошибка во время выполнения задачи");
        };
        Future<Integer> future = executor.submit(callable);
        try {
            future.get();
        } catch (InterruptedException e) {
            System.out.println("Задача была прервана: " + e.getMessage());
        } catch (ExecutionException e) {
            System.out.println("Произошла ошибка во время выполнения: " + e.getCause().getMessage());
        } finally {
            executor.shutdown();
        }
    }
}