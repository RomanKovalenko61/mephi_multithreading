package cheatsheet.synchro;

class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized void decrement() {
        count--;
    }

    public int getCount() {
        return count;
    }
}

public class SynchroEx1 {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Runnable incrementer = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        };
        Runnable decrementer = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.decrement();
            }
        };
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(incrementer);
            threads[i].start();
        }
        for (int i = 5; i < 10; i++) {
            threads[i] = new Thread(decrementer);
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println(counter.getCount());
    }
}
