package cheatsheet.projectloom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class StructuredConcurrencyExample {
    public static void main(String[] args) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) { // Создание структуры с автоматическим завершением при сбое
            // Запуск параллельных задач с помощью fork()
            var future1 = scope.fork(() -> performTask("Task 1", 2000)); // Выполняется 2 секунды
            var future2 = scope.fork(() -> performTask("Task 2", 3000)); // Выполняется 3 секунды
            // Ожидание завершения всех задач с помощью join()
            scope.join();
            // Проверка на ошибки и завершение при необходимости
            scope.throwIfFailed();
            // Получение результатов задач с использованием get()
            String result1 = future1.get(); // Результат выполнения Task 1
            String result2 = future2.get(); // Результат выполнения Task 2
            System.out.println("Results:");
            System.out.println(result1);
            System.out.println(result2);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Симуляция задачи с задержкой
    public static String performTask(String taskName, int duration) throws InterruptedException {
        Thread.sleep(duration); // Задержка для симуляции выполнения задачи
        return taskName + " completed";
    }
}
