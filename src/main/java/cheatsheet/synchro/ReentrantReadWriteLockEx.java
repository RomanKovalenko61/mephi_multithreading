package cheatsheet.synchro;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockEx {
    public static void main(String[] args) {
        Container container = new Container();
        Runnable writer = () -> {
            for (int i = 0; i < 10; i++) {
                container.increment();
                System.out.println(Thread.currentThread().getName() + " increment value");
                try {
                    Thread.sleep(new Random().nextInt(150));
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        Runnable reader = () -> {
            for (int i = 0; i < 20; i++) {
                System.out.println("Reader read " + container.get());
                try {
                    Thread.sleep(new Random().nextInt(100));
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(writer);
            threads[i].start();
        }
        for (int i = 5; i < 10; i++) {
            threads[i] = new Thread(reader);
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Container {
    private int value = 0;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = lock.writeLock();
    private Lock readLock = lock.readLock();

    public void increment() {
        writeLock.lock();
        try {
            value++;
        } finally {
            writeLock.unlock();
        }
    }

    public int get() {
        readLock.lock();
        try {
            return value;
        } finally {
            readLock.unlock();
        }
    }
}

