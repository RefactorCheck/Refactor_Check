public class netty_0031 {

        private boolean confirmShutdown() {
            if (!isShuttingDown()) {
                return false;
            }
    
            if (!inEventLoop()) {
                throw new IllegalStateException("must be invoked from an event loop");
            }
    
            cancelScheduledTasks();
    
            if (gracefulShutdownStartTime == 0) {
                gracefulShutdownStartTime = ticker.nanoTime();
            }
    
            if (runAllTasks(-1, false) > 0) {
                if (isShutdown()) {
                    // Executor shut down - no new tasks anymore.
                    return true;
                }
    
                // There were tasks in the queue. Wait a little bit more until no tasks are queued for the quiet period or
                // terminate if the quiet period is 0.
                // See https://github.com/netty/netty/issues/4241
                if (gracefulShutdownQuietPeriod == 0) {
                    return true;
                }
                return false;
            }
    
            final long nanoTime = ticker.nanoTime();
    
            if (isShutdown() || nanoTime - gracefulShutdownStartTime > gracefulShutdownTimeout) {
                return true;
            }
    
            if (nanoTime - lastExecutionTime <= gracefulShutdownQuietPeriod) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Ignore
                }
    
                return false;
            }
    
            // No tasks were added for last quiet period - hopefully safe to shut down.
            // (Hopefully because we really cannot make a guarantee that there will be no execute() calls by a user.)
            return true;
        }
}
