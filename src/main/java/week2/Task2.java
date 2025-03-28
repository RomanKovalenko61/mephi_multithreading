package week2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Task2 {
    public static void main(String[] args) throws InterruptedException {
        DiningPhilosophers diningPhilosophers = new DiningPhilosophers(5, 3);
        System.out.println(Arrays.toString(diningPhilosophers.execute(100)));
    }
}

class DiningPhilosophers {
    List<Philosopher> philosopherList = new ArrayList<>();
    List<ReentrantLock> locks = new ArrayList<>();

    List<Thread> threads = new ArrayList<>();

    public DiningPhilosophers(int philosophers, int meals) {
        Philosopher.maxNumberOfMeals = meals;
        for (int i = 0; i < philosophers; i++) {
            philosopherList.add(new Philosopher(i + 1));
            locks.add(new ReentrantLock());
        }
    }

    public int[] execute(long t) throws InterruptedException {
        for (Philosopher ph : philosopherList) {
            ReentrantLock left = locks.get(ph.getNumber() - 1);
            ReentrantLock right = locks.get((ph.getNumber() == locks.size()) ? 0 : ph.getNumber());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (ph.getNumberOfMeals() != Philosopher.maxNumberOfMeals) {
                        if (left.tryLock()) {
                            if (right.tryLock()) {
                                try {
                                    ph.incrementNumberOfMeals();
                                    Thread.sleep(t);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    left.unlock();
                                    right.unlock();
                                }
                            } else {
                                left.unlock();
                            }
                        }
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        int[] result = new int[philosopherList.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = philosopherList.get(i).getNumberOfMeals();
        }

        return result;
    }
}

class Philosopher {
    private final int number;

    private int numberOfMeals;

    static int maxNumberOfMeals;

    public Philosopher(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void incrementNumberOfMeals() {
        numberOfMeals++;
    }

    public int getNumberOfMeals() {
        return numberOfMeals;
    }
}
