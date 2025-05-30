package research.part1;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ThreadFactoryImpl implements ThreadFactory {
    private static final Logger LOG = Logger.getLogger(ThreadFactoryImpl.class.getName());
    private final AtomicInteger threadNumber = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        String threadName = "MyPool-worker-" + threadNumber.getAndIncrement();
        LOG.info("[ThreadFactory] Создание нового потока: " + threadName);
        return new Thread(r, threadName);
    }
}
