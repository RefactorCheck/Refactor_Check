public class kafka_0079 {

    private static final String WRITE_TXN_MARKERS_REQUEST = "WriteTxnMarkers request";

        private ApiResult<TopicPartition, Void> handleError(Errors error) {
            switch (error) {
                case CLUSTER_AUTHORIZATION_FAILED:
                    log.error("{} for abort spec {} failed cluster authorization", WRITE_TXN_MARKERS_REQUEST, abortSpec);
                    return ApiResult.failed(abortSpec.topicPartition(), new ClusterAuthorizationException(
                        WRITE_TXN_MARKERS_REQUEST + " with " + abortSpec + " failed due to cluster " +
                            "authorization error"));
    
                case INVALID_PRODUCER_EPOCH:
                    log.error("{} for abort spec {} failed due to an invalid producer epoch",
                        WRITE_TXN_MARKERS_REQUEST, abortSpec);
                    return ApiResult.failed(abortSpec.topicPartition(), new InvalidProducerEpochException(
                        WRITE_TXN_MARKERS_REQUEST + " with " + abortSpec + " failed due an invalid producer epoch"));
    
                case TRANSACTION_COORDINATOR_FENCED:
                    log.error("{} for abort spec {} failed because the coordinator epoch is fenced",
                        WRITE_TXN_MARKERS_REQUEST, abortSpec);
                    return ApiResult.failed(abortSpec.topicPartition(), new TransactionCoordinatorFencedException(
                        WRITE_TXN_MARKERS_REQUEST + " with " + abortSpec + " failed since the provided " +
                            "coordinator epoch " + abortSpec.coordinatorEpoch() + " has been fenced " +
                            "by the active coordinator"));
    
                case NOT_LEADER_OR_FOLLOWER:
                case REPLICA_NOT_AVAILABLE:
                case BROKER_NOT_AVAILABLE:
                case UNKNOWN_TOPIC_OR_PARTITION:
                    log.debug("{} for abort spec {} failed due to {}. Will retry after attempting to " +
                            "find the leader again", WRITE_TXN_MARKERS_REQUEST, abortSpec, error);
                    return ApiResult.unmapped(singletonList(abortSpec.topicPartition()));
    
                default:
                    log.error("{} for abort spec {} failed due to an unexpected error {}",
                        WRITE_TXN_MARKERS_REQUEST, abortSpec, error);
                    return ApiResult.failed(abortSpec.topicPartition(), error.exception(
                        WRITE_TXN_MARKERS_REQUEST + " with " + abortSpec + " failed due to unexpected error: " + error.message()));
            }
        }
}
