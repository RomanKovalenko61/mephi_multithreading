package cheatsheet.parallel.phaser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class Task1 {
    private static final int NUM_DOCUMENTS = 5;
    private static final ConcurrentLinkedQueue<String> encryptedDocuments = new ConcurrentLinkedQueue<>();
    private static final List<String> downloadedDocuments = new ArrayList<>();

    public static void main(String[] args) {
        Phaser phaser = new Phaser(1); // Главный поток зарегистрирован
        ExecutorService executor = Executors.newCachedThreadPool();
        // Первая фаза: скачивание документов
        System.out.println("Запуск первой фазы: Скачивание документов");
        for (int i = 0; i < 3; i++) {
            phaser.register();
            executor.submit(new Downloader(phaser, i + 1));
        }
        phaser.arriveAndAwaitAdvance(); // Ждем завершения первой фазы всеми потоками
        // Вторая фаза: шифрование документов
        System.out.println("Запуск второй фазы: Шифрование документов");
        for (int i = 0; i < NUM_DOCUMENTS; i++) {
            phaser.register();
            executor.submit(new Encryptor(phaser, downloadedDocuments.get(i), i + 1));
        }
        phaser.arriveAndAwaitAdvance(); // Ждем завершения второй фазы всеми потоками
        // Третья фаза: запись в базу данных
        System.out.println("Запуск третьей фазы: Запись в базу данных");
        for (int i = 0; i < 2; i++) {
            phaser.register();
            executor.submit(new DatabaseWriter(phaser, i + 1));
        }
        phaser.arriveAndAwaitAdvance(); // Ждем завершения третьей фазы всеми потоками

        executor.shutdown();
        System.out.println("Все фазы завершены, главный поток завершает работу.");
    }

    static class Downloader implements Runnable {
        private final Phaser phaser;
        private final int id;

        Downloader(Phaser phaser, int id) {
            this.phaser = phaser;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(new Random().nextInt(1000) + 500); // Имитация скачивания
                String document = "Документ " + id;
                System.out.println("Поток " + id + " скачал: " + document);
                downloadedDocuments.add(document);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                phaser.arriveAndDeregister();
            }
        }
    }

    static class Encryptor implements Runnable {
        private final Phaser phaser;
        private final String document;
        private final int id;

        Encryptor(Phaser phaser, String document, int id) {
            this.phaser = phaser;
            this.document = document;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(new Random().nextInt(1000) + 500); // Имитация шифрования
                String encryptedDocument = "Зашифрованный " + document;
                System.out.println("Поток " + id + " зашифровал: " + encryptedDocument);
                encryptedDocuments.add(encryptedDocument);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                phaser.arriveAndDeregister();
            }
        }
    }

    static class DatabaseWriter implements Runnable {
        private final Phaser phaser;
        private final int id;

        DatabaseWriter(Phaser phaser, int id) {
            this.phaser = phaser;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(new Random().nextInt(1000) + 500); // Имитация записи в базу данных
                String document = encryptedDocuments.poll();
                System.out.println("Поток " + id + " записал в базу данных: " + document);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                phaser.arriveAndDeregister();
            }
        }
    }
}
