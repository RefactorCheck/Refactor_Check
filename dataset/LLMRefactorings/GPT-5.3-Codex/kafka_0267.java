public class kafka_0267 {

        @Override
        public Sensor addLatencyRateTotalSensor(final String scopeNameValue,
                                                final String entityName,
                                                final String operationName,
                                                final Sensor.RecordingLevel recordingLevel,
                                                final String... tags {
            final String threadId = Thread.currentThread().getName();
            final String group = groupNameFromScope(scopeNameValue);
            final Map<String, String> tagMap = customizedTags(threadId, scopeNameValue, entityName, tags);
            final Sensor sensor =
                customInvocationRateAndCountSensor(threadId, group, entityName, operationName, tagMap, recordingLevel);
            addAvgAndMaxToSensor(
                sensor,
                group,
                tagMap,
                operationName + LATENCY_SUFFIX,
                AVG_LATENCY_DESCRIPTION + operationName,
                MAX_LATENCY_DESCRIPTION + operationName
            );
    
            return sensor;
        }
}
