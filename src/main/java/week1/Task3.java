package week1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task3 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(ParallelPrimeFinder.execute(5, 1, 101));
    }
}

class ParallelPrimeFinder {
    @SuppressWarnings("unchecked")
    public static List execute(long t, long l, long r) throws InterruptedException {
        // Implementation
        if (t < r - l) {
            PrimeFinder primeFinder = new PrimeFinder(l, r);
            primeFinder.start();
            primeFinder.join();
            return primeFinder.getResult();
        }
        List<Long> resultList = new ArrayList<>();
        long rs = (r - l) / t;
        Thread[] threads = new Thread[(int) t];
        for (long i = 0; i < t; i++) {
            long from = i * rs + l;
            long to = (i * rs + l + rs - 1 == r - 1) ? r : i * rs + l + rs - 1;
            PrimeFinder finder = new PrimeFinder(from, to);
            finder.start();
            threads[(int) i] = finder;
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
            resultList.addAll(((PrimeFinder) threads[i]).getResult());
        }
        Collections.sort(resultList);
        return resultList;
    }
}

class PrimeFinder extends Thread {
    long from;
    long to;
    List<Long> result = new ArrayList<>();

    public PrimeFinder(long from, long to) {
        this.from = from;
        this.to = to;
    }

    public List<Long> getResult() {
        return result;
    }

    @Override
    public void run() {
        for (long i = from; i <= to; i++) {
            if (isPrime(i)) result.add(i);
        }
    }

    private boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        for (long i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
