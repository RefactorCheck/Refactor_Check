public class dubbo_0157 {

        private void addFailed(
                LoadBalance loadbalance,
                Invocation invocation,
                List<Invoker<T>> invokers,
                Invoker<T> lastInvoker,
                URL consumerUrl) {
            initFailTimer();
            RetryTimerTask retryTimerTask = new RetryTimerTask(
                    loadbalance, invocation, invokers, lastInvoker, retries, RETRY_FAILED_PERIOD, consumerUrl);
            try {
                failTimer.newTimeout(retryTimerTask, RETRY_FAILED_PERIOD, TimeUnit.SECONDS);
            } catch (Throwable e) {
                logger.error(
                        CLUSTER_TIMER_RETRY_FAILED,
                        "add newTimeout exception",
                        "",
                        "Failback background works error, invocation->" + invocation + ", exception: " + e.getMessage(),
                        e);
            }
        }

        private void initFailTimer() {
            if (failTimer == null) {
                synchronized (this) {
                    if (failTimer == null) {
                        failTimer = new HashedWheelTimer(
                                new NamedThreadFactory("failback-cluster-timer", true),
                                1,
                                TimeUnit.SECONDS,
                                32,
                                failbackTasks);
                    }
                }
            }
        }
}
