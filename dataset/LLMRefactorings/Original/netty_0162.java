public class netty_0162 {

        private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
            if (isDone()) {
                return true;
            }
    
            if (timeoutNanos <= 0) {
                return isDone();
            }
    
            if (interruptable && Thread.interrupted()) {
                throw new InterruptedException(toString());
            }
    
            checkDeadLock();
    
            // Start counting time from here instead of the first line of this method,
            // to avoid/postpone performance cost of System.nanoTime().
            final long startTime = System.nanoTime();
            synchronized (this) {
                boolean interrupted = false;
                try {
                    long waitTime = timeoutNanos;
                    while (!isDone() && waitTime > 0) {
                        incWaiters();
                        try {
                            wait(waitTime / 1000000, (int) (waitTime % 1000000));
                        } catch (InterruptedException e) {
                            if (interruptable) {
                                throw e;
                            } else {
                                interrupted = true;
                            }
                        } finally {
                            decWaiters();
                        }
                        // Check isDone() in advance, try to avoid calculating the elapsed time later.
                        if (isDone()) {
                            return true;
                        }
                        // Calculate the elapsed time here instead of in the while condition,
                        // try to avoid performance cost of System.nanoTime() in the first loop of while.
                        waitTime = timeoutNanos - (System.nanoTime() - startTime);
                    }
                    return isDone();
                } finally {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
}
