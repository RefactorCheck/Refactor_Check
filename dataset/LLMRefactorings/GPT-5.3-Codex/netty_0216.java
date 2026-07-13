public class netty_0216 {

        @Override
        public void run() {
            assert executor().inEventLoop();
            try {
                if (delayNanos() > 0L) {
                    // Not yet expired, need to add or remove from queue
                    if (isCancelled()) {
                        scheduledExecutor().scheduledTaskQueue().removeTyped(this);
                    } else {
                        scheduledExecutor().scheduleFromEventLoop(this);
                    }
                    return;
                }
                if (periodNanos == 0) {
                    if (setUncancellableInternal()) {

                        setSuccessInternal((runTask()));
                    }
                } else {
                    // check if is done as it may was cancelled
                    if (!isCancelled()) {
                        runTask();
                        if (!executor().isShutdown()) {
                            if (periodNanos > 0) {
                                deadlineNanos += periodNanos;
                            } else {
                                deadlineNanos = scheduledExecutor().getCurrentTimeNanos() - periodNanos;
                            }
                            if (!isCancelled()) {
                                scheduledExecutor().scheduleFromEventLoop(this);
                            }
                        }
                    }
                }
            } catch (Throwable cause) {
                setFailureInternal(cause);
            }
        }
}
