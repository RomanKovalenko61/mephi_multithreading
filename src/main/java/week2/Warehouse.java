package week2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Warehouse {

    Map<String, Integer> storage = Collections.synchronizedMap(new HashMap<>());

    Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Warehouse warehouse1 = new Warehouse();
        warehouse1.addItem("apple", 100);
        System.out.println(warehouse1.getItemCount("apple") != 0);
        System.out.println(warehouse1.removeItem("apple", 50));

        Warehouse warehouse2 = new Warehouse();
        warehouse1.transfer(warehouse2, "apple", 30);
        System.out.println("warehouse 1 get apple: " + warehouse1.getItemCount("apple"));
        System.out.println("warehouse 2 get apple: " + warehouse2.getItemCount("apple"));
    }

    public boolean addItem(String itemType, int count) {
        lock.lock();
        try {
            if (count <= 0) return false;
            Integer result = storage.put(itemType, storage.getOrDefault(itemType, 0) + count);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeItem(String itemType, int count) {
        lock.lock();
        try {
            if (!storage.containsKey(itemType)) return false;
            Integer existCount = storage.get(itemType);
            if (existCount - count < 0) return false;
            storage.put(itemType, existCount - count);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public int getItemCount(String itemType) {
        lock.lock();
        try {
            return storage.getOrDefault(itemType, 0);
        } finally {
            lock.unlock();
        }
    }

    public boolean transfer(Warehouse other, String itemType, int count) {
        if (this.lock.tryLock()) {
            if (other.lock.tryLock()) {
                try {
                    Integer existCount = this.storage.getOrDefault(itemType, 0);
                    if (existCount - count < 0) return false;
                    this.removeItem(itemType, count);
                    other.addItem(itemType, count);
                    return true;
                } finally {
                    this.lock.unlock();
                    other.lock.unlock();
                }
            } else {
                this.lock.unlock();
            }
        }
        return false;
    }
}
