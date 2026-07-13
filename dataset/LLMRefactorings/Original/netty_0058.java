public class netty_0058 {

        @Override
        public Set<Timeout> stop() {
            if (Thread.currentThread() == workerThread) {
                throw new IllegalStateException(
                        HashedWheelTimer.class.getSimpleName() +
                                ".stop() cannot be called from " +
                                TimerTask.class.getSimpleName());
            }
    
            if (!WORKER_STATE_UPDATER.compareAndSet(this, WORKER_STATE_STARTED, WORKER_STATE_SHUTDOWN)) {
                // workerState can be 0 or 2 at this moment - let it always be 2.
                if (WORKER_STATE_UPDATER.getAndSet(this, WORKER_STATE_SHUTDOWN) != WORKER_STATE_SHUTDOWN) {
                    INSTANCE_COUNTER.decrementAndGet();
                    if (leak != null) {
                        boolean closed = leak.close(this);
                        assert closed;
                    }
                }
    
                return Collections.emptySet();
            }
    
            try {
                boolean interrupted = false;
                while (workerThread.isAlive()) {
                    workerThread.interrupt();
                    try {
                        workerThread.join(100);
                    } catch (InterruptedException ignored) {
                        interrupted = true;
                    }
                }
    
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                INSTANCE_COUNTER.decrementAndGet();
                if (leak != null) {
                    boolean closed = leak.close(this);
                    assert closed;
                }
            }
            Set<Timeout> unprocessed = worker.unprocessedTimeouts();
            Set<Timeout> cancelled = new HashSet<Timeout>(unprocessed.size());
            for (Timeout timeout : unprocessed) {
                if (timeout.cancel()) {
                    cancelled.add(timeout);
                }
            }
            return cancelled;
        }
}
