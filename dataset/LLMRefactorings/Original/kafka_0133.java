public class kafka_0133 {

        public void throttle() {
            if (targetThroughput == 0) {
                try {
                    synchronized (this) {
                        while (!wakeup) {
                            this.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                }
                return;
            }
    
            // throttle throughput by sleeping, on average,
            // (1 / this.throughput) seconds between "things sent"
            sleepDeficitNs += sleepTimeNs;
    
            // If enough sleep deficit has accumulated, sleep a little
            if (sleepDeficitNs >= MIN_SLEEP_NS) {
                long sleepStartNs = System.nanoTime();
                try {
                    synchronized (this) {
                        long remaining = sleepDeficitNs;
                        while (!wakeup && remaining > 0) {
                            long sleepMs = remaining / 1000000;
                            long sleepNs = remaining - sleepMs * 1000000;
                            this.wait(sleepMs, (int) sleepNs);
                            long elapsed = System.nanoTime() - sleepStartNs;
                            remaining = sleepDeficitNs - elapsed;
                        }
                        wakeup = false;
                    }
                    sleepDeficitNs = 0;
                } catch (InterruptedException e) {
                    // If sleep is cut short, reduce deficit by the amount of
                    // time we actually spent sleeping
                    long sleepElapsedNs = System.nanoTime() - sleepStartNs;
                    if (sleepElapsedNs <= sleepDeficitNs) {
                        sleepDeficitNs -= sleepElapsedNs;
                    }
                }
            }
        }
}
