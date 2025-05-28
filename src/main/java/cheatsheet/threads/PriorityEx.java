package cheatsheet.threads;

public class PriorityEx {
    public static void main(String[] args) {
        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        };
        Thread highPriorityThread = new Thread(runnable, "High Priority Thread");
        Thread lowPriorityThread = new Thread(runnable, "Low Priority Thread");
        highPriorityThread.setPriority(Thread.MAX_PRIORITY);
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY);

        lowPriorityThread.start();
        highPriorityThread.start();
    }
}
