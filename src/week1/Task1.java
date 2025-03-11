package week1;

public class Task1 {
    public static void main(String[] args) throws InterruptedException {
        int t = 2;
        long[] array = {1, 3, 5, 7, 9};
        System.out.println(ParallelMaxFinder.execute(t, array));

        int t1 = 2;
        long[] array1 = {-5, 0, 12, 3, 8, -2, 15, 7, 6, 4};
        System.out.println(ParallelMaxFinder.execute(t1, array1));

        int t2 = 2;
        long[] array2 = {100, 200, 300, 400, 500, 600, 700, 800};
        System.out.println(ParallelMaxFinder.execute(t2, array2));

        int t3 = 2;
        long[] array3 = {};
        System.out.println(ParallelMaxFinder.execute(t3, array3));

        int t4 = 2;
        long[] array4 = {42};
        System.out.println(ParallelMaxFinder.execute(t4, array4));

        int t5 = 2;
        long[] array5 = {-10, -20, -5, -30};
        System.out.println(ParallelMaxFinder.execute(t5, array5));
    }
}

class ParallelMaxFinder {
    public static long execute(int t, long[] array) throws InterruptedException {
        // Implementation
        if (array.length == 0) {
            return 0;
        }
        if (array.length < 4) {
            long ans = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] > ans) ans = array[i];
            }
            return ans;
        }
        long[] answer = new long[t];
        Thread[] threads = new Thread[t];
        int rs = array.length / t;
        for (int i = 0; i < t; i++) {
            int from = (i == 0) ? 0 : i * rs + 1;
            int to = (i == t - 1) ? array.length - 1 : i * rs + rs;
            threads[i] = new Finder(i, array, from, to, answer);
            threads[i].start();
        }
        for (int i = 0; i < t; i++) {
            threads[i].join();
        }
        long ans = answer[0];
        for (int i = 1; i < t; i++) {
            if (answer[i] > ans) ans = answer[i];
        }
        return ans;
    }
}

class Finder extends Thread {
    int thread;
    long[] array;
    int from;
    int to;
    long[] answer;

    public Finder(int thread, long[] array, int from, int to, long[] answer) {
        this.thread = thread;
        this.array = array;
        this.from = from;
        this.to = to;
        this.answer = answer;
    }

    public void run() {
        long max = array[from];
        for (int i = from + 1; i <= to; i++) {
            if (array[i] > max) max = array[i];
        }
        answer[thread] = max;
    }
}