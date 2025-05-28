package cheatsheet.synchro;

import java.util.*;

public class SynchroCoollectionEx {
    public static void main(String[] args) throws InterruptedException {
        Collection<Integer> syncCollection = Collections.synchronizedCollection(new ArrayList<>());
        Runnable listOperations = () -> {
            syncCollection.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
        };

        Thread thread1 = new Thread(listOperations);
        Thread thread2 = new Thread(listOperations);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println("Size of synchronized collection: " + syncCollection.size());

        List<String> syncCollectionLetters = Collections.synchronizedList(Arrays.asList("a", "b", "c"));
        List<String> uppercasedCollection = new ArrayList<>();


        // Работа с синхронизированными коллекциями
        // все операции с коллекцией автоматически синхронизируются;
        // гарантируется атомарность отдельных операций;
        // сохраняется функциональность оригинальной коллекции;
        // операции итерирования требуют дополнительной синхронизации.
        Runnable listIter = () -> {
            synchronized (syncCollectionLetters) {
                syncCollectionLetters.forEach(s -> uppercasedCollection.add(s.toUpperCase()));
            }
        };

        Thread iterThread = new Thread(listIter);
        iterThread.start();
        iterThread.join();
        uppercasedCollection.forEach(System.out::println);
    }
}
