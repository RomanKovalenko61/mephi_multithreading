package week2;

public class Task1 {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000);
        System.out.println(account.execute(2, 2, 50, 100));

        BankAccount account1 = new BankAccount(500);
        System.out.println(account1.execute(3, 2, 150, 200));

        BankAccount account2 = new BankAccount(0);
        System.out.println(account2.execute(1, 1, 500, 1000));
    }
}

class BankAccount {

    int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    public int execute(int n, int m, int withdrawalAmount, int depositAmount) {
        Thread[] threads = new Thread[n + m];
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(() -> withdraw(withdrawalAmount));
        }
        for (int i = 0; i < m; i++) {
            threads[i + n] = new Thread(() -> deposit(depositAmount));
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return getBalance();
    }

    public synchronized void withdraw(int amount) {
        balance -= amount;
    }

    public synchronized void deposit(int amount) {
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }
}