package cheatsheet.threads;

class WorkerTask implements Runnable {
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            // Выполнение некоторой работы
            for (int i = 1; i <= 5; i++) {
                System.out.println(threadName + " выполняет шаг " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + " был прерван.");
        }
        System.out.println(threadName + " завершен.");
    }
}

public class ThreadGroupEx {
    public static void main(String[] args) {
        ThreadGroup group = new ThreadGroup("WorkerGroup");
        Thread thread1 = new Thread(group, new WorkerTask(), "Worker1");
        Thread thread2 = new Thread(group, new WorkerTask(), "Worker2");

        thread1.start();
        thread2.start();
        System.out.println("Активных потоков в группе: " + group.activeCount());
        // Ждем 2 секунды перед прерыванием
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Прерываем все потоки в группе
        group.interrupt();
        // Ожидаем завершения потоков
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Главный поток завершен.");
    }
}
