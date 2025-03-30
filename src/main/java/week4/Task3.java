package week4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Task3 {

    static List<Long> results = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
//        System.out.println("Count prime numbers from 0 to 100 = " + ForkJoinPrimeTask.countPrimes(0, 100));
//        System.out.println("Count prime numbers from 7 to 7 = " + ForkJoinPrimeTask.countPrimes(7, 7));
        System.out.println("Count prime numbers from 8 to 8 = " + ForkJoinPrimeTask.countPrimes(8, 8));
        System.out.println("List of results : " + results);
    }

    static class ForkJoinPrimeTask {
        public static long countPrimes(long l, long r) {
            // Ваше решение
            ForkJoinPool pool = new ForkJoinPool();
            FinderPrimeNumbers task = new FinderPrimeNumbers(l, r);
            return pool.invoke(task);
        }
    }

    static class FinderPrimeNumbers extends RecursiveTask<Long> {

        private final long start;

        private final long end;

        public FinderPrimeNumbers(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            int THRESHOLD = 15;
            if (end - start < THRESHOLD) {
                long counter = 0;
                for (long i = start; i <= end; i++) {
                    if (i < 2) continue;
                    if (i == 2) {
                        counter++;
                        results.add(2L);
                        continue;
                    }
                    boolean isPrime = true;
                    for (long j = 2; j <= Math.sqrt(i); j++) {
                        if (i % j == 0) {
                            isPrime = false;
                            break;
                        }
                    }
                    if (isPrime) {
                        counter++;
                        results.add(i);
                    }
                }
                return counter;
            } else {
                long mid = (start + end) >> 1;
                FinderPrimeNumbers leftTask = new FinderPrimeNumbers(start, mid);
                FinderPrimeNumbers rightTask = new FinderPrimeNumbers(mid + 1, end);
                leftTask.fork();
                rightTask.fork();
                return leftTask.join() + rightTask.join();
            }
        }
    }
}
