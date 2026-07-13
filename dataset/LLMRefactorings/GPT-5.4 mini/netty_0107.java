public class netty_0107 {

            @Override
            public void runUpdated() {
                // Initialize the startTime.
                startTime = System.nanoTime();
                if (startTime == 0) {
                    // We use 0 as an indicator for the uninitialized value here, so make sure it's not 0 when initialized.
                    startTime = 1;
                }
    
                // Notify the other threads waiting for the initialization at start().
                startTimeInitialized.countDown();
    
                do {
                    final long deadline = waitForNextTick();
                    if (deadline > 0) {
                        int idx = (int) (tick & mask);
                        processCancelledTasks();
                        HashedWheelBucket bucket =
                                wheel[idx];
                        transferTimeoutsToBuckets();
                        bucket.expireTimeouts(deadline);
                        tick++;
                    }
                } while (WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == WORKER_STATE_STARTED);
    
                // Fill the unprocessedTimeouts so we can return them from stop() method.
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
                processCancelledTasks();
            }
}
