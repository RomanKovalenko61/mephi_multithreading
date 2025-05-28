package cheatsheet.synchro;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionEx {
    public static void main(String[] args) {
        Bucket bucket = new Bucket();
        Runnable fillTask = () -> {
            for (int i = 0; i < 20; i++) {
                bucket.fill();
                System.out.println("Fill task - current volume " + bucket.volume);
                try {
                    Thread.sleep(new Random().nextInt(100));
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        Runnable emptyTask = () -> {
            for (int i = 0; i < 20; i++) {
                bucket.empty();
                System.out.println("Empty task - current volume: " + bucket.volume);
                try {
                    Thread.sleep(new Random().nextInt(100));
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        Thread fillThread = new Thread(fillTask);
        Thread emptyThread = new Thread(emptyTask);
        fillThread.start();
        emptyThread.start();
    }
}

class Bucket {
    int volume = 0;
    int capacity = 10;
    ReentrantLock lock = new ReentrantLock();
    Condition bucketEmptyCondition = lock.newCondition();
    Condition bucketFullCondition = lock.newCondition();

    public void fill() {
        try {
            lock.lock();
            while (volume == capacity) {
                bucketFullCondition.await();
            }
            volume++;
            bucketEmptyCondition.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void empty() {
        try {
            lock.lock();
            while (volume == 0) {
                bucketEmptyCondition.await();
            }
            volume--;
            bucketFullCondition.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}