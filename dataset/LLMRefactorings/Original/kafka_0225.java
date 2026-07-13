public class kafka_0225 {

        public boolean add(TimerTaskEntry timerTaskEntry) {
            long expiration = timerTaskEntry.expirationMs;
    
            if (timerTaskEntry.cancelled()) {
                // Cancelled
                return false;
            } else if (expiration < currentTimeMs + tickMs) {
                // Already expired
                return false;
            } else if (expiration < currentTimeMs + interval) {
                // Put in its own bucket
                long virtualId = expiration / tickMs;
                int bucketId = (int) (virtualId % (long) wheelSize);
                TimerTaskList bucket = buckets[bucketId];
                bucket.add(timerTaskEntry);
    
                // Set the bucket expiration time
                if (bucket.setExpiration(virtualId * tickMs)) {
                    // The bucket needs to be enqueued because it was an expired bucket
                    // We only need to enqueue the bucket when its expiration time has changed, i.e. the wheel has advanced
                    // and the previous buckets gets reused; further calls to set the expiration within the same wheel cycle
                    // will pass in the same value and hence return false, thus the bucket with the same expiration will not
                    // be enqueued multiple times.
                    queue.offer(bucket);
                }
    
                return true;
            } else {
                // Out of the interval. Put it into the parent timer
                if (overflowWheel == null) addOverflowWheel();
                return overflowWheel.add(timerTaskEntry);
            }
        }
}
