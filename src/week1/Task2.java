package week1;

import java.math.BigInteger;

public class Task2 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(ParallelFactorial.execute(1, 5));
        System.out.println(ParallelFactorial.execute(3, 5));
        System.out.println(ParallelFactorial.execute(2, 0));
        System.out.println(ParallelFactorial.execute(2, 1));
        System.out.println(ParallelFactorial.execute(10, 100000));
    }
}

class ParallelFactorial {
    public static BigInteger execute(int t, int n) throws InterruptedException {
        // Implementation
        if (n < 2) {
            return BigInteger.ONE;
        }
        if (t < n * 2) {
            Factorial factorial = new Factorial(1, n);
            factorial.start();
            factorial.join();
            return factorial.getResult();
        }
        BigInteger[] answers = new BigInteger[t];
        Thread[] threads = new Thread[t];
        int rs = n / t;
        for (int i = 0; i < t; i++) {
            int from = i * rs + 1;
            int to = (i == t - 1) ? n : i * rs + rs;
            threads[i] = new Factorial(from, to);
            threads[i].start();
        }
        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
            result = result.multiply(((Factorial) threads[i]).getResult());
        }
        return result;
    }
}

class Factorial extends Thread {
    int from;
    int to;
    BigInteger result;

    public Factorial(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public BigInteger getResult() {
        return result;
    }

    @Override
    public void run() {
        result = BigInteger.ONE;
        for (int i = from; i <= to; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
    }
}