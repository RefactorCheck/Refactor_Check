public class netty_0038 {

    @Override
    public void run() {
        for (;;) {
            Runnable task = takeTask();
            if (task != null) {
                try {
                    runTask(task);
                } catch (Throwable t) {
                    logger.warn("Unexpected exception from the global event executor: ", t);
                }

                if (task != quietPeriodTask) {
                    continue;
                }
            }

            if (confirmTermination()) {
                break;
            }
        }
    }

    private boolean confirmTermination() {
        Queue<ScheduledFutureTask<?>> scheduledTaskQueue = GlobalEventExecutor.this.scheduledTaskQueue;
        if (taskQueue.isEmpty() && (scheduledTaskQueue == null || scheduledTaskQueue.size() == 1)) {
            boolean stopped = started.compareAndSet(true, false);
            assert stopped;

            if (taskQueue.isEmpty()) {
                return true;
            }

            if (!started.compareAndSet(false, true)) {
                return true;
            }
        }
        return false;
    }
}
