public class dubbo_0051 {

        private static void endCount(RpcStatus status, long elapsed, boolean succeeded) {
            status.active.decrementAndGet();
            status.total.incrementAndGet();
            status.totalElapsed.addAndGet(elapsed);
    
            updateMax(status.maxElapsed, elapsed);
    
            if (succeeded) {
                updateMax(status.succeededMaxElapsed, elapsed);
            } else {
                status.failed.incrementAndGet();
                status.failedElapsed.addAndGet(elapsed);
                updateMax(status.failedMaxElapsed, elapsed);
            }
        }
        
        private static void updateMax(AtomicLong maxElapsed, long elapsed) {
            if (maxElapsed.get() < elapsed) {
                maxElapsed.set(elapsed);
            }
        }
}
