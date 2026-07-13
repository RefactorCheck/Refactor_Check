public class dubbo_0276 {

    private static final int MANDATORY_SELECTION_TIMEOUT_MULTIPLIER = 2;

    public double getLoad(String idKey, int weight, int timeout) {
        AdaptiveMetrics metrics = getStatus(idKey);

        // If the time more than 2 times, mandatory selected
        if (System.currentTimeMillis() - metrics.pickTime > timeout * MANDATORY_SELECTION_TIMEOUT_MULTIPLIER) {
            return 0;
        }

        if (metrics.currentTime > 0) {
            long multiple = (System.currentTimeMillis() - metrics.currentTime) / timeout + 1;
            if (multiple > 0) {
                if (metrics.currentProviderTime == metrics.currentTime) {
                    // penalty value
                    metrics.lastLatency = timeout * MANDATORY_SELECTION_TIMEOUT_MULTIPLIER;
                } else {
                    metrics.lastLatency = metrics.lastLatency >> multiple;
                }
                metrics.ewma = metrics.beta * metrics.ewma + (1 - metrics.beta) * metrics.lastLatency;
                metrics.currentTime = System.currentTimeMillis();
            }
        }

        long inflight = metrics.consumerReq.get() - metrics.consumerSuccess.get() - metrics.errorReq.get();
        return metrics.providerCPULoad
                * (Math.sqrt(metrics.ewma) + 1)
                * (inflight + 1)
                / ((((double) metrics.consumerSuccess.get() / (double) (metrics.consumerReq.get() + 1)) * weight) + 1);
    }
}
