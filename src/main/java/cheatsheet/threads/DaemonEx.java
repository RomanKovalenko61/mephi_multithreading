package cheatsheet.threads;

class DaemonTask implements Runnable {
    @Override
    public void run() {
        if (Thread.currentThread().isDaemon()) {
            System.out.println(Thread.currentThread().getName() + " Поток является Daemon-потоком.");
        } else {
            System.out.println("Поток является пользовательским потоком.");
        }
        while (true) {
            System.out.println("Daemon-поток выполняется...");
            try {
                Thread.sleep(1000); // Сон на 1 секунду
            } catch (InterruptedException e) {
                System.out.println("Daemon-поток был прерван.");
                break;
            }
        }
        System.out.println("Daemon-поток завершен.");
    }
}

public class DaemonEx {
    public static void main(String[] args) {
        Thread daemonThread = new Thread(new DaemonTask());
        daemonThread.setDaemon(true); // Устанавливаем поток как Daemon
        daemonThread.start();
        System.out.println(Thread.currentThread().getName() + " Главный поток выполняется...");
        try {
            Thread.sleep(3000); // Главный поток работает 3 секунды
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Главный поток завершен.");
        // После завершения главного потока Daemon-поток автоматически завершится
    }
}
