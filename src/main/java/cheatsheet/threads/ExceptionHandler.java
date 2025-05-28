package cheatsheet.threads;

public class ExceptionHandler {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println("Глобальный обработчик: исключение в потоке " + t.getName() + ": " + e.getMessage());
        });
        Thread thread = new Thread(() -> {
            throw new RuntimeException("Ошибка в потоке");
        });
        thread.start();

        Thread thread2 = new Thread(() -> {
            throw new RuntimeException("Ошибка в потоке");
        });
        thread2.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Исключение в потоке " + t.getName() + ": " + e.getMessage());
        });
        thread2.start();
    }
}
