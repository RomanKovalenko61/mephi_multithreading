package cheatsheet.parallel.forkJoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Task2 {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        int[] array = new int[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1; // Заполняем массив числами от 1 до 100
        }
        SquareTask task = new SquareTask(array, 0, array.length);
        pool.invoke(task);
        System.out.println("Массив после возведения в квадрат:");
        for (int value : array) {
            System.out.print(value + " ");
        }
    }

    static class SquareTask extends RecursiveAction {
        private final int[] array;
        private final int start;
        private final int end;
        private static final int THRESHOLD = 10;

        public SquareTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    array[i] *= array[i];
                }
            } else {
                int mid = (start + end) / 2;
                SquareTask leftTask = new SquareTask(array, start, mid);
                SquareTask rightTask = new SquareTask(array, mid, end);
                leftTask.fork(); // Асинхронно запускаем левую подзадачу
                rightTask.fork(); // Асинхронно запускаем правую подзадачу
                leftTask.join(); // Ожидаем завершения левой подзадачи
                rightTask.join(); // Ожидаем завершения правой подзадачи
            }
        }
    }
}
