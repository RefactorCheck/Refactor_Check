public class dubbo_0184 {

        @Override
        public void run(Timeout timeout) throws Exception {
            if (timeout.isCancelled() || timeout.timer().isStop() || isCancel()) {
                // other thread cancel this timeout or stop the timer.
                return;
            }
            if (retryTimes > 0 && times > retryTimes) {
                // 1-13 - failed to execute the retrying task.
    
                logger.warn(
                        REGISTRY_EXECUTE_RETRYING_TASK,
                        "registry center offline",
                        "Check the registry server.",
                        "Final failed to execute task " + taskName + ", url: " + url + ", retry " + retryTimes + " times.");
    
                return;
            }
            if (logger.isInfoEnabled()) {
                logger.info(taskName + " : " + url);
            }
            try {
                if (!registry.isAvailable()) {
                    throw new IllegalStateException("Registry is not available.");
                }
                doRetry(url, registry, timeout);
            } catch (Throwable t) {
                handleRetryException(t, timeout);
            }
        }

        private void handleRetryException(Throwable t, Timeout timeout) {
            // 1-13 - failed to execute the retrying task.
            logger.warn(
                    REGISTRY_EXECUTE_RETRYING_TASK,
                    "registry center offline",
                    "Check the registry server.",
                    "Failed to execute task " + taskName + ", url: " + url + ", waiting for again, cause:"
                            + t.getMessage(),
                    t);
            reput(timeout, retryPeriod);
        }
}
