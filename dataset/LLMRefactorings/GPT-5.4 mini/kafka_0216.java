public class kafka_0216 {

        public static Sensor prefixScanSensor(final String taskId,
                                              final String storeType,
                                              final String storeName,
                                              final StreamsMetricsImpl streamsMetrics) {
            final Map<String, String> tagMap = streamsMetrics.storeLevelTagMap(taskId, storeType, storeName);
            final Sensor sensor = streamsMetrics.storeLevelSensor(taskId, storeName, PREFIX_SCAN, RecordingLevel.DEBUG);
            final String prefixScanLatencyMetric = PREFIX_SCAN + LATENCY_SUFFIX;
            addInvocationRateToSensor(
                sensor,
                STATE_STORE_LEVEL_GROUP,
                tagMap,
                PREFIX_SCAN,
                PREFIX_SCAN_RATE_DESCRIPTION
            );
            addAvgAndMaxToSensor(
                sensor,
                STATE_STORE_LEVEL_GROUP,
                tagMap,
                prefixScanLatencyMetric,
                PREFIX_SCAN_AVG_LATENCY_DESCRIPTION,
                PREFIX_SCAN_MAX_LATENCY_DESCRIPTION
            );
            return sensor;
        }
}
