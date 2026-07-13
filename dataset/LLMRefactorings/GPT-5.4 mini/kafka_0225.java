public class kafka_0225 {

        public boolean add(TimerTaskEntry timerTaskEntry) {
            long expiration = timerTaskEntry.expirationMs;
            long nextTickExpiration = currentTimeMs + tickMs;
            long intervalExpiration = currentTimeMs + interval;

            if (timerTaskEntry.cancelled()) {
                return false;
            } else if (expiration < nextTickExpiration) {
                return false;
            } else if (expiration < intervalExpiration) {
                long virtualId = expiration / tickMs;
                int bucketId = (int) (virtualId % (long) wheelSize);
                TimerTaskList bucket = buckets[bucketId];
                bucket.add(timerTaskEntry);

                if (bucket.setExpiration(virtualId * tickMs)) {
                    queue.offer(bucket);
                }

                return true;
            } else {
                if (overflowWheel == null) addOverflowWheel();
                return overflowWheel.add(timerTaskEntry);
            }
        }
}
