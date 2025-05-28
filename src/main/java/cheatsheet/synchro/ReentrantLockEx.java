package cheatsheet.synchro;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockEx {
    public static void main(String[] args) {
        CommonResource commonResource = new CommonResource();
        ReentrantLock reentrantLock = new ReentrantLock();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new CountThread(reentrantLock, commonResource));
            thread.setName("Thread " + i);
            thread.start();
        }
    }
}

class CommonResource {
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

class CountThread implements Runnable {
    CommonResource resource;
    ReentrantLock lock;

    public CountThread(ReentrantLock lock, CommonResource resource) {
        this.lock = lock;
        this.resource = resource;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            resource.setCount(1);
            for (int i = 0; i < 5; i++) {
                System.out.printf("%s %d \n", Thread.currentThread().getName(), resource.getCount());
                resource.setCount(resource.getCount() + 1);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}