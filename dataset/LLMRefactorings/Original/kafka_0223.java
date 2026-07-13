public class kafka_0223 {

        @Override
        public void close() {
            Arrays.asList(
                NUM_OFFSETS,
                NUM_CLASSIC_GROUPS,
                NUM_CLASSIC_GROUPS_PREPARING_REBALANCE,
                NUM_CLASSIC_GROUPS_COMPLETING_REBALANCE,
                NUM_CLASSIC_GROUPS_STABLE,
                NUM_CLASSIC_GROUPS_DEAD,
                NUM_CLASSIC_GROUPS_EMPTY
            ).forEach(registry::removeMetric);
    
            Arrays.asList(
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
            ).forEach(metrics::removeMetric);
    
            Arrays.asList(
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
            ).forEach(metrics::removeSensor);
        }
}
