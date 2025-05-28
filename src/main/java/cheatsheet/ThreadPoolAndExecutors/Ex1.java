package cheatsheet.ThreadPoolAndExecutors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Ex1 {
    public static void main(String[] args) {
        //        FixedThreadPool
        //        CachedThreadPool
        //        SingleThreadExecutor
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Callable<Integer>> tasks = Arrays.asList(
                () -> 1,
                () -> 2,
                () -> 3
        );

        try {
            List<Future<Integer>> results = executorService.invokeAll(tasks);
            for (Future<Integer> result : results) {
                System.out.println("Result: " + result.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
