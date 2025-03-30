package week4;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Task1 {
    public static void main(String[] args) {
        System.out.println("Factorial 5 = " + factorial(5));
        System.out.println("Factorial 10 = " + factorial(10));
        System.out.println("Factorial 15 = " + factorial(15));
        System.out.println("Factorial 20 = " + factorial(20));
    }

    public static long factorial(long n) {
        ForkJoinPool pool = new ForkJoinPool();
        long[] array = new long[(int) n];
        for (int i = 0; i < n; i++) {
            array[i] = i + 1;
        }
        Multiply task = new Multiply(array, 0, array.length);
        return pool.invoke(task);
    }

    static class Multiply extends RecursiveTask<Long> {
        private final long[] array;
        private final int start;
        private final int end;
        private static final int THRESHOLD = 5;

        public Multiply(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                long result = 1;
                for (int i = start; i < end; i++) {
                    result *= array[i];
                }
                return result;
            } else {
                int mid = (start + end) >> 1;
                Multiply left = new Multiply(array, start, mid);
                Multiply right = new Multiply(array, mid, end);
                left.fork();
                right.fork();
                long leftResult = left.join();
                long rightResult = right.join();
                return leftResult * rightResult;
            }
        }
    }
}
