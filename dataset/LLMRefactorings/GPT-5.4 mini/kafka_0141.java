public class kafka_0141 {

        protected void collectMetric(MetricsEmitter metricsEmitter, MetricKey metricKey, KafkaMetric metric) {
            Object metricValue;
    
            try {
                metricValue = metric.metricValue();
            } catch (Exception e) {
                // If an exception occurs when retrieving value, log warning and continue to process the rest of metrics
                log.warn("Failed to retrieve metric value {}", metricKey.name(), e);
                return;
            }
    
            Instant now = Instant.ofEpochMilli(time.milliseconds());
            boolean measurableMetric = metric.isMeasurable();
            if (measurableMetric) {
                Measurable measurable = metric.measurable();
                Double value = (Double) metricValue;
    
                if (measurable instanceof WindowedCount || measurable instanceof CumulativeSum) {
                    collectSum(metricKey, value, metricsEmitter, now);
                } else {
                    collectGauge(metricKey, value, metricsEmitter, now);
                }
            } else {
                // It is non-measurable Gauge metric.
                // Collect the metric only if its value is a number.
                if (metricValue instanceof Number) {
                    Number value = (Number) metricValue;
                    collectGauge(metricKey, value, metricsEmitter, now);
                } else {
                    // skip non-measurable metrics
                    log.debug("Skipping non-measurable gauge metric {}", metricKey.name());
                }
            }
        }
}
