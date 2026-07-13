public class netty_0216 {

        @Override
        public void run() {
            assert executor().inEventLoop();
            try {
                if (delayNanos() > 0L) {
                    handleDelayedTask();
                    return;
                }
                if (periodNanos == 0) {
                    executeOnce();
                } else {
                    executeRecurring();
                }
            } catch (Throwable cause) {
                setFailureInternal(cause);
            }
        }

        private void handleDelayedTask() {
            if (isCancelled()) {
                scheduledExecutor().scheduledTaskQueue().removeTyped(this);
            } else {
                scheduledExecutor().scheduleFromEventLoop(this);
            }
        }

        private void executeOnce() {
            if (setUncancellableInternal()) {
                V result = runTask();
                setSuccessInternal(result);
            }
        }

        private void executeRecurring() {
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
}
