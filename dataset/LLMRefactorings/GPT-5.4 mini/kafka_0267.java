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
            final String latencyOperationName = operationName + LATENCY_SUFFIX;
            addAvgAndMaxToSensor(
                sensor,
                group,
                tagMap,
                latencyOperationName,
                AVG_LATENCY_DESCRIPTION + operationName,
                MAX_LATENCY_DESCRIPTION + operationName
            );

            return sensor;
        }
}
