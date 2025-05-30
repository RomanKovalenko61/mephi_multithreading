package research.part1;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryImpl implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        String threadName = "MyPool-worker-" + threadNumber.getAndIncrement();
        System.out.println("[ThreadFactory] Создание нового потока: " + threadName);
        return new Thread(r, threadName);
    }
}
