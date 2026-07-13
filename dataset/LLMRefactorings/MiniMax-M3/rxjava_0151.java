public class rxjava_0151 {

    @Override
    public void start() {
        ScheduledExecutorService next = null;
        for (;;) {
            ScheduledExecutorService current = executor.get();
            if (current != SHUTDOWN) {
                shutdownIfNotNull(next);
                return;
            }
            if (next == null) {
                next = createExecutor(threadFactory);
            }
            if (executor.compareAndSet(current, next)) {
                return;
            }

        }
    }

    private void shutdownIfNotNull(ScheduledExecutorService executor) {
        if (executor != null) {
            executor.shutdown();
        }
    }
}
