public class netty_0024 {

        public long readTimeToWait(final long size, final long limitTraffic, final long maxTime, final long now) {
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
                long time = sum * 1000 / limitTraffic - interval + pastDelay;
                return handleTimeCalculation(time, sum, interval, pastDelay, now, maxTime, localReadingTime);
            }
            long lastsum = sum + lastRB;
            long lastinterval = interval + checkInterval.get();
            long time = lastsum * 1000 / limitTraffic - lastinterval + pastDelay;
            return handleTimeCalculation(time, lastsum, lastinterval, pastDelay, now, maxTime, localReadingTime);
        }

        private long handleTimeCalculation(long time, long sum, long interval, long pastDelay, long now, long maxTime, long localReadingTime) {
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
}
