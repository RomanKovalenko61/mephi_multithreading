package cheatsheet.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Начало выполнения");
        long start = System.currentTimeMillis();
        Incrementer incrementer = new Incrementer();
        for (int i = 0; i < 20; i++) { //создали 20 потоков, каждый увеличил на 1000
            IncrementerThread ct = new IncrementerThread(incrementer);
            ct.start();
        }
        Thread.sleep(10_000);
        System.out.println("Counter:" + incrementer.getAmount()); //должно быть 20_000
        System.out.println("Время выполнения: " + (System.currentTimeMillis() - start) + " мс");
    }
}

class Incrementer {
    //private List<Object> list =  new ArrayList<>(); //это плохой пример, так делать не надо
    // CopyOnWriteArrayList лучше использовать когда очень частые операции чтения и редкие изменения
    private final List<Object> list = new CopyOnWriteArrayList<>(new ArrayList<>());


    public void increaseObjectAmount() {
        list.add(new Object());
    }

    public long getAmount() {
        return list.size();
    }

    public void remove() { // выбрасывает java.lang.UnsupportedOperationException
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.remove();
        }
    }
}

class IncrementerThread extends Thread {
    private final Incrementer incrementer;

    public IncrementerThread(Incrementer incrementer) {
        this.incrementer = incrementer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            incrementer.increaseObjectAmount();
        }
    }
}
