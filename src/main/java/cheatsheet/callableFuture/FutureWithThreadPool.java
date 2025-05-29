package cheatsheet.callableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FutureWithThreadPool {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Callable<Integer> callable = new MyCallable();
            futures.add(executor.submit(callable));
        }
        for (Future<Integer> future : futures) {
            try {
                System.out.println("Результат: " + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }
}

class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        // Выполнение некоторой логики
        Thread.sleep(1000); // Имитация длительной задачи
        return (int) (Math.random() * 100); // Возвращаем случайное число
    }
}
