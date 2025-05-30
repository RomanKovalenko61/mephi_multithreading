package research.part1;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        CustomThreadPool threadPool = new CustomThreadPool(2, 4, 5, TimeUnit.SECONDS, 5, 2);
        threadPool.setRejectPolicy(new CallerRunsPolicy());

        for (int i = 0; i < 100; i++) {
            final int taskNumber = i;
            threadPool.execute(() -> {
                randomSleep();
                LOG.info("[Main] Task #" + taskNumber + " is running in " + Thread.currentThread().getName());
            });
        }

        Thread.sleep(1000);
        threadPool.shutdownNow();
        threadPool.execute(() -> {
            System.out.println("!!!! Task after shutdown attempt.");
        });
    }

    public static void randomSleep() {
        try {
            Thread.sleep(new Random().nextInt(6000));
        } catch (InterruptedException e) {
            LOG.warning("[MAIN] Interrupt task when it sleep");
        }
    }
}
