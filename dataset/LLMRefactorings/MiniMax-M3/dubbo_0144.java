public class dubbo_0144 {

    private static final long NANOS_PER_MILLI = 1000000L;
    private static final long ROUND_UP_OFFSET = NANOS_PER_MILLI - 1;
    private static final long WINDOWS_SLEEP_GRANULARITY_MS = 10L;

    private long waitForNextTick() {
        long deadline = tickDuration * (tick + 1);

        for (; ; ) {
            final long currentTime = System.nanoTime() - startTime;
            long sleepTimeMs = (deadline - currentTime + ROUND_UP_OFFSET) / NANOS_PER_MILLI;

            if (sleepTimeMs <= 0) {
                if (currentTime == Long.MIN_VALUE) {
                    return -Long.MAX_VALUE;
                } else {
                    return currentTime;
                }
            }
            if (isWindows()) {
                sleepTimeMs = sleepTimeMs / WINDOWS_SLEEP_GRANULARITY_MS * WINDOWS_SLEEP_GRANULARITY_MS;
            }

            try {
                Thread.sleep(sleepTimeMs);
            } catch (InterruptedException ignored) {
                if (WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == WORKER_STATE_SHUTDOWN) {
                    return Long.MIN_VALUE;
                }
            }
        }
    }
}
