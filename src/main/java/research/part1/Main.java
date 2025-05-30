package research.part1;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CustomThreadPool threadPool = new CustomThreadPool(2, 4, 10, TimeUnit.SECONDS, 10, 2);

        for (int i = 0; i < 5; i++) {
            final int taskNumber = i;
            threadPool.execute(() -> {
                randomSleep();
                System.out.println("[Main] Task #" + taskNumber + " is running in " + Thread.currentThread().getName());
            });
        }

        Thread.sleep(3000);
        threadPool.shutdown();
    }

    public static void randomSleep() {
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
