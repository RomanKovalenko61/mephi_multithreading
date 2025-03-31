package week5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Task2 {
    public static void main(String[] args) {
        int[][] A = {{1, 2, 3}, {4, 5, 6}};
        int[][] B = {{7, 8}, {9, 10}, {11, 12}};

        printArray(MatrixMultiplier.multiply(A, B)); // expected = {{58, 64},{139, 154}};
    }

    public static void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            int[] arr = array[i];
            for (int j = 0; j < array.length; j++) {
                System.out.print(arr[j] + " ");
            }
            System.out.println();
        }
    }

    static class MatrixMultiplier {
        public static int[][] multiply(int[][] a, int[][] b) {
            // Ваше решение
            if (a.length == 0 || b.length == 0) {
                System.err.println("Внимание! Пустая матрица");
                return new int[][]{};
            }

            if (a[0].length != b.length) {
                throw new IllegalArgumentException("Данные матрицы умножить нельзя!!!");
            }

            int rowsA = a.length;
            int colsB = b[0].length;
            int commonLength = a[0].length;

            int[][] result = new int[rowsA][colsB];

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            for (int i = 0; i < rowsA; i++) {
                final int currentRow = i;
                executor.submit(() -> {
                    for (int j = 0; j < colsB; j++) {
                        int localResult = 0;
                        for (int k = 0; k < commonLength; k++) {
                            localResult += a[currentRow][k] * b[k][j];
                        }
                        synchronized (result) {
                            result[currentRow][j] += localResult;
                        }
                    }
                });
            }

            try {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
