public class netty_0024 {

        public static long readTimeToWait(final long size, final long limitTraffic, final long maxTime, final long now) {
            bytesRecvFlowControl(size);
            if (size == 0 || limitTraffic == 0) {
                return 0;
            }
            final long lastTimeCheck = lastTime.get();
            long sum = currentReadBytes.get();
            long localReadingTime = readingTime;
            long lastRB = lastReadBytes;
            final long interval = now - lastTimeCheck;
            long pastDelay = Math.max(lastReadingTime - lastTimeCheck, 0);
            if (interval > AbstractTrafficShapingHandler.MINIMAL_WAIT) {
                // Enough interval time to compute shaping
                long time = sum * 1000 / limitTraffic - interval + pastDelay;
                if (time > AbstractTrafficShapingHandler.MINIMAL_WAIT) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Time: " + time + ':' + sum + ':' + interval + ':' + pastDelay);
                    }
                    if (time > maxTime && now + time - localReadingTime > maxTime) {
                        time = maxTime;
                    }
                    readingTime = Math.max(localReadingTime, now + time);
                    return time;
                }
                readingTime = Math.max(localReadingTime, now);
                return 0;
            }
            // take the last read interval check to get enough interval time
            long lastsum = sum + lastRB;
            long lastinterval = interval + checkInterval.get();
            long time = lastsum * 1000 / limitTraffic - lastinterval + pastDelay;
            if (time > AbstractTrafficShapingHandler.MINIMAL_WAIT) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Time: " + time + ':' + lastsum + ':' + lastinterval + ':' + pastDelay);
                }
                if (time > maxTime && now + time - localReadingTime > maxTime) {
                    time = maxTime;
                }
                readingTime = Math.max(localReadingTime, now + time);
                return time;
            }
            readingTime = Math.max(localReadingTime, now);
            return 0;
        }
}
