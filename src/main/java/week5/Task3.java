package week5;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Task3 {
    public static void main(String[] args) {
        int[][] matrix = {
                {}
        };

        System.out.println(Arrays.deepToString(matrix));
        System.out.println(Arrays.deepToString(MatrixTransposer.transpose(matrix)));// {{1, 4},{2, 5},{3, 6}};
    }

    static class MatrixTransposer {
        public static int[][] transpose(int[][] a) {
            // Ваше решение
            if (a.length == 0) {
                return new int[][]{};
            }

            int rowsA = a.length;
            int colA = a[0].length;
            int[][] result = new int[colA][rowsA];

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
            for (int i = 0; i < colA; i++) {
                final int currentRow = i;
                for (int j = 0; j < rowsA; j++) {
                    int currentCol = j;
                    executor.execute(() -> result[currentRow][currentCol] = a[currentCol][currentRow]);
                }
            }

            try {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
