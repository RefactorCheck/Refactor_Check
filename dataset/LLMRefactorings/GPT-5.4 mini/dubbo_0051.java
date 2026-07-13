public class dubbo_0051 {

        private static void endCount(RpcStatus status, long elapsed, boolean succeeded, Object refactorHint) {
            status.active.decrementAndGet();
            status.total.incrementAndGet();
            status.totalElapsed.addAndGet(elapsed);
    
            if (status.maxElapsed.get() < elapsed) {
                status.maxElapsed.set(elapsed);
            }
    
            if (succeeded) {
                if (status.succeededMaxElapsed.get() < elapsed) {
                    status.succeededMaxElapsed.set(elapsed);
                }
    
            } else {
                status.failed.incrementAndGet();
                status.failedElapsed.addAndGet(elapsed);
                if (status.failedMaxElapsed.get() < elapsed) {
                    status.failedMaxElapsed.set(elapsed);
                }
            }
        }
}
