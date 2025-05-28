package cheatsheet.ThreadPoolAndExecutors;

import java.util.concurrent.*;

public class CallableExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Callable<Integer> task = () -> {
            Thread.sleep(1000);
            return 123;
        };
        Future<Integer> future = executor.submit(task);

        try {
            Integer result = future.get(); // Ожидание выполнения задачи и получение результата
            System.out.println("Result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
