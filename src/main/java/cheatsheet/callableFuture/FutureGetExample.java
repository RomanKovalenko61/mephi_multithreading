package cheatsheet.callableFuture;

import java.util.concurrent.*;

public class FutureGetExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = new ExampleCallable();
        Future<String> future = executor.submit(callable);
        System.out.println("Задача отправлена на выполнение...");
        try {
            String result = future.get(); // Блокируется, пока результат не станет доступен
            System.out.println("Результат: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}

class ExampleCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        // Выполнение некоторой логики
        Thread.sleep(500);
        return "Задача выполнена успешно";
    }
}