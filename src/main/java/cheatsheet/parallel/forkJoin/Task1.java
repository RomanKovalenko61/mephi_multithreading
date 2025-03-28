package cheatsheet.parallel.forkJoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Task1 {

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        int[] array = new int[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1; // Заполняем массив числами от 1 до 100
        }
        SumTask task = new SumTask(array, 0, array.length);
        int result = pool.invoke(task);
        System.out.println("Сумма элементов массива: " + result);
    }

    static class SumTask extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start;
        private final int end;
        private static final int THRESHOLD = 10;

        public SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= THRESHOLD) {
                int sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else {
                int mid = (start + end) / 2;
                SumTask leftTask = new SumTask(array, start, mid);
                SumTask rightTask = new SumTask(array, mid, end);
                leftTask.fork(); // Асинхронно запускаем левую подзадачу
                rightTask.fork(); // Асинхронно запускаем правую подзадачу
                int leftResult = leftTask.join(); // Ожидаем завершения левой подзадачи и получаем результат
                int rightResult = rightTask.join(); // Ожидаем завершения правой подзадачи и получаем результат
                return leftResult + rightResult;
            }
        }
    }
}
