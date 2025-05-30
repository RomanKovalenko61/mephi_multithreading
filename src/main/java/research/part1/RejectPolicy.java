package research.part1;

import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;

@FunctionalInterface
public interface RejectPolicy {
    void handle(Runnable task);
}

class AbortPolicy implements RejectPolicy {
    private static final Logger log = Logger.getLogger(AbortPolicy.class.getName());

    @Override
    public void handle(Runnable task) {
        log.warning("AbortPolicy: Task rejected");
        throw new RejectedExecutionException("Task rejected with agreed policy: ");
    }
}

class CallerRunsPolicy implements RejectPolicy {
    private static final Logger log = Logger.getLogger(CallerRunsPolicy.class.getName());
    @Override
    public void handle(Runnable task) {
        if (task != null) {
            log.info("CallerRunsPolicy: Running task in caller thread: " + Thread.currentThread().getName());
            task.run();
            log.info("CallerRunsPolicy: Finished task in caller thread: " + Thread.currentThread().getName());
        }
    }
}