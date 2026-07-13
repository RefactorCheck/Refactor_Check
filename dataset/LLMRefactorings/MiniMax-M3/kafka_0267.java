public class kafka_0267 {

        @Override
        public Sensor addLatencyRateTotalSensor(final String scopeName,
                                                final String entityName,
                                                final String operationName,
                                                final Sensor.RecordingLevel recordingLevel,
                                                final String... tags) {
            final String threadId = Thread.currentThread().getName();
            final String group = groupNameFromScope(scopeName);
            final Map<String, String> tagMap = customizedTags(threadId, scopeName, entityName, tags);
            final Sensor sensor =
                customInvocationRateAndCountSensor(threadId, group, entityName, operationName, tagMap, recordingLevel);
            final String latencyMetricName = operationName + LATENCY_SUFFIX;
            final String avgLatencyDescription = AVG_LATENCY_DESCRIPTION + operationName;
            final String maxLatencyDescription = MAX_LATENCY_DESCRIPTION + operationName;
            addAvgAndMaxToSensor(
                sensor,
                group,
                tagMap,
                latencyMetricName,
                avgLatencyDescription,
                maxLatencyDescription
            );
    
            return sensor;
        }
}
