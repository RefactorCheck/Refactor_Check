public class kafka_0223 {

        @Override
        public void close() {
            removeRegistryMetrics(
                NUM_OFFSETS,
                NUM_CLASSIC_GROUPS,
                NUM_CLASSIC_GROUPS_PREPARING_REBALANCE,
                NUM_CLASSIC_GROUPS_COMPLETING_REBALANCE,
                NUM_CLASSIC_GROUPS_STABLE,
                NUM_CLASSIC_GROUPS_DEAD,
                NUM_CLASSIC_GROUPS_EMPTY
            );

            removeMetrics(
                offsetCountMetricName,
                classicGroupCountMetricName,
                classicGroupCountPreparingRebalanceMetricName,
                classicGroupCountCompletingRebalanceMetricName,
                classicGroupCountStableMetricName,
                classicGroupCountDeadMetricName,
                classicGroupCountEmptyMetricName,
                consumerGroupCountMetricName,
                consumerGroupCountEmptyMetricName,
                consumerGroupCountAssigningMetricName,
                consumerGroupCountReconcilingMetricName,
                consumerGroupCountStableMetricName,
                consumerGroupCountDeadMetricName,
                shareGroupCountMetricName,
                shareGroupCountEmptyMetricName,
                shareGroupCountStableMetricName,
                shareGroupCountDeadMetricName,
                streamsGroupCountMetricName,
                streamsGroupCountEmptyMetricName,
                streamsGroupCountAssigningMetricName,
                streamsGroupCountReconcilingMetricName,
                streamsGroupCountStableMetricName,
                streamsGroupCountDeadMetricName,
                streamsGroupCountNotReadyMetricName
            );

            removeSensors(
                OFFSET_COMMITS_SENSOR_NAME,
                OFFSET_EXPIRED_SENSOR_NAME,
                OFFSET_DELETIONS_SENSOR_NAME,
                CLASSIC_GROUP_COMPLETED_REBALANCES_SENSOR_NAME,
                CONSUMER_GROUP_REBALANCES_SENSOR_NAME,
                SHARE_GROUP_REBALANCES_SENSOR_NAME,
                STREAMS_GROUP_REBALANCES_SENSOR_NAME,
                STREAMS_GROUP_TOPOLOGY_DESCRIPTION_CLEANUP_CYCLE_RUNS_SENSOR_NAME,
                STREAMS_GROUP_TOPOLOGY_DESCRIPTION_CLEANUP_ELIGIBLE_GROUPS_SENSOR_NAME,
                STREAMS_GROUP_TOPOLOGY_DESCRIPTION_DELETE_SUCCESS_SENSOR_NAME,
                STREAMS_GROUP_TOPOLOGY_DESCRIPTION_DELETE_ERROR_SENSOR_NAME
            );
        }

        private void removeRegistryMetrics(String... metricNames) {
            Arrays.asList(metricNames).forEach(registry::removeMetric);
        }

        private void removeMetrics(String... metricNames) {
            Arrays.asList(metricNames).forEach(metrics::removeMetric);
        }

        private void removeSensors(String... sensorNames) {
            Arrays.asList(sensorNames).forEach(metrics::removeSensor);
        }
}
