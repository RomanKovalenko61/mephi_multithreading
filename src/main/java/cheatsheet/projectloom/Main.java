package cheatsheet.projectloom;

import java.util.concurrent.Executors;

// works in my machine with 21 version --enable-preview
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = Thread.ofVirtual().start(() -> {
            System.out.println("Running in a virtual thread");
        });
        thread.join();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> System.out.println("Task 1 in virtual thread"));
            executor.submit(() -> System.out.println("Task 2 in virtual thread"));
        }

        Thread.startVirtualThread(() -> {
            System.out.println("Running with startVirtualThread");
        });

        Thread.sleep(1000);
    }
}
