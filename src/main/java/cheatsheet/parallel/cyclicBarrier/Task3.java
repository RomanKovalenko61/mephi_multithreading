package cheatsheet.parallel.cyclicBarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Task3 {
    private static final int NUM_CYCLES = 3;
    // Список зашифрованных частей документа, который в данный момент шифруется
    private static final ConcurrentLinkedQueue<String> encryptedParts = new ConcurrentLinkedQueue<>();
    // Список всех зашифрованных документов
    private static final List<String> finalResults = new ArrayList<>();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Random random = new Random();
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("Все части текста зашифрованы. Объединяем их...");
            StringBuilder combinedResult = new StringBuilder();
            encryptedParts.forEach(combinedResult::append);
            finalResults.add(combinedResult.toString());
            encryptedParts.clear();
            System.out.println("Текст успешно зашифрован и сохранен в результатах.");
        });
        Runnable task = () -> {
            try {
                for (int i = 0; i < NUM_CYCLES; i++) {
                    // Симуляция шифрования части текста
                    Thread.sleep(random.nextInt(3000) + 1000); // Имитация долгого шифрования
                    String encryptedPart = "Часть текста " + Thread.currentThread().getName() + " (цикл " + (i + 1) + ")";
                    System.out.println(Thread.currentThread().getName() + ": Зашифровал часть текста: " + encryptedPart);
                    encryptedParts.add(encryptedPart);
                    // Поток ждет, пока остальные завершат шифрование своих частей
                    barrier.await();
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        };
        executor.submit(task);
        executor.submit(task);
        executor.submit(task);
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        // Вывод всех окончательных результатов
        System.out.println("Итоговые зашифрованные тексты:");
        finalResults.forEach(System.out::println);
    }
}
