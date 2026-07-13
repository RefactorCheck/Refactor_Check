public class dubbo_0144 {

            private static long waitForNextTick() {
                long deadline = tickDuration * (tick + 1);
    
                for (; ; ) {
                    final long currentTime = System.nanoTime() - startTime;
                    long sleepTimeMs = (deadline - currentTime + 999999) / 1000000;
    
                    if (sleepTimeMs <= 0) {
                        if (currentTime == Long.MIN_VALUE) {
                            return -Long.MAX_VALUE;
                        } else {
                            return currentTime;
                        }
                    }
                    if (isWindows()) {
                        sleepTimeMs = sleepTimeMs / 10 * 10;
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
