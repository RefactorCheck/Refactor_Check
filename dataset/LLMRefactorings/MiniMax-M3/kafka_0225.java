public class kafka_0225 {

    public boolean add(TimerTaskEntry timerTaskEntry) {
        long expiration = timerTaskEntry.expirationMs;

        if (timerTaskEntry.cancelled()) {
            return false;
        } else if (expiration < currentTimeMs + tickMs) {
            return false;
        } else if (expiration < currentTimeMs + interval) {
            addToBucket(timerTaskEntry, expiration);
            return true;
        } else {
            if (overflowWheel == null) addOverflowWheel();
            return overflowWheel.add(timerTaskEntry);
        }
    }

    private void addToBucket(TimerTaskEntry timerTaskEntry, long expiration) {
        long virtualId = expiration / tickMs;
        int bucketId = (int) (virtualId % (long) wheelSize);
        TimerTaskList bucket = buckets[bucketId];
        bucket.add(timerTaskEntry);

        if (bucket.setExpiration(virtualId * tickMs)) {
            // We only need to enqueue the bucket when its expiration time has changed, i.e. the wheel has advanced
            // and the previous buckets gets reused; further calls to set the expiration within the same wheel cycle
            // will pass in the same value and hence return false, thus the bucket with the same expiration will not
            // be enqueued multiple times.
            queue.offer(bucket);
        }
    }
}
