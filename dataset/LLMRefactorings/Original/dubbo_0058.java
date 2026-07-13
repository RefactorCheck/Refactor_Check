public class dubbo_0058 {

        private void onRTEvent(RequestEvent event) {
            MethodMetric metric =
                    new MethodMetric(applicationModel, event.getAttachmentValue(MetricsConstants.INVOCATION), serviceLevel);
            long responseTime = event.getTimePair().calc();
            if (enableRt) {
                TimeWindowQuantile quantile = rt.get(metric);
                if (quantile == null) {
                    quantile = ConcurrentHashMapUtils.computeIfAbsent(
                            rt, metric, k -> new TimeWindowQuantile(DEFAULT_COMPRESSION, bucketNum, timeWindowSeconds));
                    samplesChanged.set(true);
                }
                quantile.add(responseTime);
            }
    
            if (enableRtPxx) {
                TimeWindowAggregator timeWindowAggregator = rtAgr.get(metric);
                if (timeWindowAggregator == null) {
                    timeWindowAggregator = ConcurrentHashMapUtils.computeIfAbsent(
                            rtAgr, metric, methodMetric -> new TimeWindowAggregator(bucketNum, timeWindowSeconds));
                    samplesChanged.set(true);
                }
                timeWindowAggregator.add(responseTime);
            }
        }
}
