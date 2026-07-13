public class netty_0107 {

    @Override
    public void run() {
        startTime = System.nanoTime();
        if (startTime == 0) {
            startTime = 1;
        }

        startTimeInitialized.countDown();

        do {
            final long deadline = waitForNextTick();
            if (deadline > 0) {
                int idx = (int) (tick & mask);
                processCancelledTasks();
                HashedWheelBucket bucket = wheel[idx];
                transferTimeoutsToBuckets();
                bucket.expireTimeouts(deadline);
                tick++;
            }
        } while (WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == WORKER_STATE_STARTED);

        fillUnprocessedTimeouts();
        processCancelledTasks();
    }

    private void fillUnprocessedTimeouts() {
        for (HashedWheelBucket bucket: wheel) {
            bucket.clearTimeouts(unprocessedTimeouts);
        }
        for (;;) {
            HashedWheelTimeout timeout = timeouts.poll();
            if (timeout == null) {
                break;
            }
            if (!timeout.isCancelled()) {
                unprocessedTimeouts.add(timeout);
            }
        }
    }
}
