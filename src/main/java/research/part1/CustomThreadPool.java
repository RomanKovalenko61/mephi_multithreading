package research.part1;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CustomThreadPool implements CustomExecutor {
    private int corePoolSize;
    private int maxPoolSize;
    private int keepAliveTime;
    private TimeUnit timeUnit;
    private int queueSize;
    private int minSpareThreads;

    public CustomThreadPool(int corePoolSize, int maxPoolSize, int keepAliveTime, TimeUnit timeUnit,
                            int queueSize, int minSpareThreads) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.queueSize = queueSize;
        this.minSpareThreads = minSpareThreads;
    }

    @Override
    public void execute(Runnable command) {

    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdownNow() {

    }
}
