package cheatsheet.synchro;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicEx {
    public static void main(String[] args) {
        SafeCounterWithoutLock counter = new SafeCounterWithoutLock();
        Runnable incrementer = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        };
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(incrementer);
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Final counter value: " + counter.getValue());
    }
}

class SafeCounterWithoutLock {
    private final AtomicInteger counter = new AtomicInteger(0);

    int getValue() {
        return counter.get();
    }

    void increment() {
        counter.incrementAndGet();
    }
}
