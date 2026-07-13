public class kafka_0193 {

        public static Set<MetricName> brokerTopicStatsMetrics() {
            Set<MetricName> metrics = new HashSet<>();
            addRemoteMetrics(metrics);
            addLagMetrics(metrics);
            return metrics;
        }

        private static void addRemoteMetrics(Set<MetricName> metrics) {
            metrics.add(REMOTE_COPY_BYTES_PER_SEC_METRIC);
            metrics.add(REMOTE_FETCH_BYTES_PER_SEC_METRIC);
            metrics.add(REMOTE_FETCH_REQUESTS_PER_SEC_METRIC);
            metrics.add(REMOTE_COPY_REQUESTS_PER_SEC_METRIC);
            metrics.add(REMOTE_DELETE_REQUESTS_PER_SEC_METRIC);
            metrics.add(BUILD_REMOTE_LOG_AUX_STATE_REQUESTS_PER_SEC_METRIC);
            metrics.add(FAILED_REMOTE_FETCH_PER_SEC_METRIC);
            metrics.add(FAILED_REMOTE_COPY_PER_SEC_METRIC);
            metrics.add(REMOTE_LOG_METADATA_COUNT_METRIC);
            metrics.add(REMOTE_LOG_SIZE_COMPUTATION_TIME_METRIC);
            metrics.add(REMOTE_LOG_SIZE_BYTES_METRIC);
            metrics.add(FAILED_REMOTE_DELETE_PER_SEC_METRIC);
            metrics.add(FAILED_BUILD_REMOTE_LOG_AUX_STATE_PER_SEC_METRIC);
        }

        private static void addLagMetrics(Set<MetricName> metrics) {
            metrics.add(REMOTE_COPY_LAG_BYTES_METRIC);
            metrics.add(REMOTE_COPY_LAG_SEGMENTS_METRIC);
            metrics.add(REMOTE_DELETE_LAG_BYTES_METRIC);
            metrics.add(REMOTE_DELETE_LAG_SEGMENTS_METRIC);
        }
}
