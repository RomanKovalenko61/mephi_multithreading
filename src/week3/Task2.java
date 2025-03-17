package week3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Task2 {
    static String TESTS_FILE_PATH = "src/week3/testdata.txt";

    static Map<Integer, User> users = Collections.synchronizedMap(new HashMap<>());

    record Transactional(int fromId, int toId, int amount) {
    }

    static class User {
        final int id;
        int balance;

        public User(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }
    }

    static Transactional parse(String s) {
        String[] parsed = s.trim().replaceAll("\\s+", "").split("-");
        return new Transactional(Integer.parseInt(parsed[0]), Integer.parseInt(parsed[2]), Integer.parseInt(parsed[1]));
    }

    static synchronized void doTransact(Transactional transactional) throws Exception {
        User user1 = users.get(transactional.fromId);
        User user2 = users.get(transactional.toId);
        Objects.requireNonNull(user1, "Транзакция отменена! fromId: "
                + transactional.fromId + " not found user with this ID");
        Objects.requireNonNull(user2, "Транзакция отменена! ToId: "
                + transactional.toId + " not found user with this ID");
        System.out.println("Thread info: " + Thread.currentThread().getName());
        if (user1.balance - transactional.amount < 0) {
            throw new Exception("Недостаточно средств. Баланс " + user1.balance + " Сумма перевода " + transactional.amount);
        }
        System.out.println("Переводим " + transactional.amount + " со счета userId " + transactional.fromId
                + " на счет userId " + transactional.toId);
        user1.balance -= transactional.amount;
        user2.balance += transactional.amount;
    }

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(TESTS_FILE_PATH));
        } catch (FileNotFoundException e) {
            System.out.println("File isn't found: " + TESTS_FILE_PATH);
        }
        int numberOfUsers = Objects.requireNonNull(scanner).nextInt();
        for (int i = 0; i < numberOfUsers; i++) {
            users.put(i, new User(i, scanner.nextInt()));
        }
        int numberOfTransactional = scanner.nextInt();
        scanner.nextLine();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < numberOfTransactional; i++) {
            Transactional transactional = parse(scanner.nextLine());
            executorService.submit(() -> {
                try {
                    doTransact(transactional);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            });
        }
        executorService.shutdown();
        try {
            var termination = executorService.awaitTermination(1, TimeUnit.MINUTES);
            if (!termination) {
                System.out.println("Что-то пошло не так...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numberOfUsers; i++) {
            System.out.println("User " + i + " final balance: " + users.get(i).balance);
        }
    }
}