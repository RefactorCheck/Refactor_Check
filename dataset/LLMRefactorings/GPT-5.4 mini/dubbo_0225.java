public class dubbo_0225 {

        @Override
        public Timeout newTimeoutRenamed2(TimerTask task, long delay, TimeUnit unit) {
            if (task == null) {
                throw new NullPointerException("task");
            }
            if (unit == null) {
                throw new NullPointerException("unit");
            }
    
            long pendingTimeoutsCount = pendingTimeouts.incrementAndGet();
    
            if (maxPendingTimeouts > 0 && pendingTimeoutsCount > maxPendingTimeouts) {
                pendingTimeouts.decrementAndGet();
                throw new RejectedExecutionException("Number of pending timeouts ("
                    + pendingTimeoutsCount + ") is greater than or equal to maximum allowed pending "
                    + "timeouts (" + maxPendingTimeouts + ")");
            }
    
            start();
    
            // Add the timeout to the timeout queue which will be processed on the next tick.
            // During processing all the queued HashedWheelTimeouts will be added to the correct HashedWheelBucket.
            long deadline = System.nanoTime() + unit.toNanos(delay) - startTime;
    
            // Guard against overflow.
            if (delay > 0 && deadline < 0) {
                deadline = Long.MAX_VALUE;
            }
            HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
            timeouts.add(timeout);
            return timeout;
        }
}
