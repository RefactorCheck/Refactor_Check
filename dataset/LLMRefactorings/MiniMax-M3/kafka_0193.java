public class kafka_0193 {

    public static Set<MetricName> brokerTopicStatsMetrics() {
        return new HashSet<>(Arrays.asList(allMetricNames()));
    }

    private static MetricName[] allMetricNames() {
        return new MetricName[] {
            REMOTE_COPY_BYTES_PER_SEC_METRIC,
            REMOTE_FETCH_BYTES_PER_SEC_METRIC,
            REMOTE_FETCH_REQUESTS_PER_SEC_METRIC,
            REMOTE_COPY_REQUESTS_PER_SEC_METRIC,
            REMOTE_DELETE_REQUESTS_PER_SEC_METRIC,
            BUILD_REMOTE_LOG_AUX_STATE_REQUESTS_PER_SEC_METRIC,
            FAILED_REMOTE_FETCH_PER_SEC_METRIC,
            FAILED_REMOTE_COPY_PER_SEC_METRIC,
            REMOTE_LOG_METADATA_COUNT_METRIC,
            REMOTE_LOG_SIZE_COMPUTATION_TIME_METRIC,
            REMOTE_LOG_SIZE_BYTES_METRIC,
            FAILED_REMOTE_DELETE_PER_SEC_METRIC,
            FAILED_BUILD_REMOTE_LOG_AUX_STATE_PER_SEC_METRIC,
            REMOTE_COPY_LAG_BYTES_METRIC,
            REMOTE_COPY_LAG_SEGMENTS_METRIC,
            REMOTE_DELETE_LAG_BYTES_METRIC,
            REMOTE_DELETE_LAG_SEGMENTS_METRIC,
        };
    }
}
