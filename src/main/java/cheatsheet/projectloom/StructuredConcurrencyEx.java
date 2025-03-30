package cheatsheet.projectloom;

import java.util.concurrent.StructuredTaskScope;

public class StructuredConcurrencyEx {
    private static int counter = 0;

    // Синхронизированный метод увеличения счетчика
    public static synchronized void increment() {
        counter++;
    }

    public static void main(String[] args) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Запускаем несколько виртуальных потоков, выполняющих параллельное увеличение счетчика
            for (int i = 0; i < 10; i++) {
                scope.fork(() -> {
                    for (int j = 0; j < 1000; j++) {
                        increment();  // Обеспечивает потокобезопасный доступ
                    }
                    return null;
                });
            }
            // Ожидаем завершения всех задач
            scope.join();
            // Выводим итоговое значение счетчика
            System.out.println("Final counter value: " + counter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
