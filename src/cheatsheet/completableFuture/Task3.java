package cheatsheet.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Task3 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 21 * 2);
        Thread.sleep(500);
        if (future.isDone()) {
            System.out.println("future is success");
            System.out.println("Result future: " + future.get());
        }

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Ошибка выполнения"); // Supplier don't throw exceptions
        });
        future1.exceptionally(ex -> {
            System.out.println("Задача завершилась с ошибкой: " + ex.getMessage());
            return null;
        });
        if (future1.isCompletedExceptionally()) {
            System.out.println("Задача завершилась с ошибкой");
        }

        //Используйте join(), когда уверены в отсутствии исключений, и get() для более детализированной обработки.

        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> 333);
        int result = future3.join(); // join() не выбрасывает проверяемых исключений (checked exceptions), тогда как get() требует их обработки.
//        int result = future3.get();
        System.out.println("Результат: " + result);

        CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> 444);
        future4.whenComplete((result2, exception) -> {
            if (exception == null) {
                System.out.println("Задача завершена успешно. Результат: " + result2);
            } else {
                System.out.println("Произошла ошибка: " + exception.getMessage());
            }
        });

        // В лекции вместо Object Integer, но тогда ошибка компиляции

        CompletableFuture<Object> future5 = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Ошибка выполнения future5");
        }).exceptionally(ex -> {
            System.out.println("Произошла ошибка: " + ex.getMessage());
            return 0;
        });
        System.out.println("Результат после обработки ошибки: " + future5.join()); // Результат: 0

        CompletableFuture<Object> future6 = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Ошибка выполнения");
        }).handle((result2, exception) -> {
            if (exception != null) {
                System.out.println("Обработали ошибку: " + exception.getMessage());
                return 0;
            } else {
                return result2;
            }
        });
        System.out.println("Результат: " + future6.join()); // Результат: 0
    }
}
