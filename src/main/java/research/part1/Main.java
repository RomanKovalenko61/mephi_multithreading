package research.part1;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        CustomThreadPool threadPool = new CustomThreadPool(2, 5, 10, TimeUnit.SECONDS, 10, 2);
        threadPool.setRejectPolicy(new CallerRunsPolicy());

        for (int i = 0; i < 20; i++) {
            final int taskNumber = i;
            threadPool.execute(() -> {
                randomSleep();
                LOG.info("[Main] Task #" + taskNumber + " is running in " + Thread.currentThread().getName());
            });
        }

        Thread.sleep(3000);
        threadPool.shutdown();
        threadPool.execute(() -> {
            System.out.println("!!!! Task after shutdown attempt.");
        });
    }

    public static void randomSleep() {
        try {
            Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
