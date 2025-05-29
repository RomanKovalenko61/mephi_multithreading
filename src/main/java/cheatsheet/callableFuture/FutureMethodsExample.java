package cheatsheet.callableFuture;

import java.util.concurrent.*;

public class FutureMethodsExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = new ExampleCallable();
        Future<String> future = executor.submit(callable);

        try {
            // Проверяем, завершена ли задача
            if (!future.isDone()) {
                System.out.println("Задача все еще выполняется...");
            }
            // Получаем результат выполнения задачи
            String result = future.get(1, TimeUnit.SECONDS);
            System.out.println("Результат: " + result);
        } catch (InterruptedException e) {
            System.out.println("Задача была прервана: " + e.getMessage());
        } catch (ExecutionException e) {
            System.out.println("Ошибка во время выполнения: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            System.out.println("Превышено время ожидания результата");
        } finally {
            // Отменяем задачу, если она еще не завершена
            if (!future.isDone()) {
                future.cancel(true);
            }
            executor.shutdown();
        }
    }
}
