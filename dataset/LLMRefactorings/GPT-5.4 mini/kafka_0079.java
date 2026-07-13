public class kafka_0079 {

        private ApiResult<TopicPartition, Void> handleError(Errors error) {
            switch (error) {
                case CLUSTER_AUTHORIZATION_FAILED:
                    log.error("WriteTxnMarkers request for abort spec {} failed cluster authorization", abortSpec);
                    return ApiResult.failed(abortSpec.topicPartition(), new ClusterAuthorizationException(
                        "WriteTxnMarkers request with " + abortSpec + " failed due to cluster " +
                            "authorization error"));

                case INVALID_PRODUCER_EPOCH:
                    log.error("WriteTxnMarkers request for abort spec {} failed due to an invalid producer epoch",
                        abortSpec);
                    return ApiResult.failed(abortSpec.topicPartition(), new InvalidProducerEpochException(
                        "WriteTxnMarkers request with " + abortSpec + " failed due an invalid producer epoch"));

                case TRANSACTION_COORDINATOR_FENCED:
                    log.error("WriteTxnMarkers request for abort spec {} failed because the coordinator epoch is fenced",
                        abortSpec);
                    return ApiResult.failed(abortSpec.topicPartition(), new TransactionCoordinatorFencedException(
                        "WriteTxnMarkers request with " + abortSpec + " failed since the provided " +
                            "coordinator epoch " + abortSpec.coordinatorEpoch() + " has been fenced " +
                            "by the active coordinator"));

                case NOT_LEADER_OR_FOLLOWER:
                case REPLICA_NOT_AVAILABLE:
                case BROKER_NOT_AVAILABLE:
                case UNKNOWN_TOPIC_OR_PARTITION:
                    log.debug("WriteTxnMarkers request for abort spec {} failed due to {}. Will retry after attempting to " +
                            "find the leader again", abortSpec, error);
                    return ApiResult.unmapped(singletonList(abortSpec.topicPartition()));

                default:
                    return handleUnexpectedError(error);
            }
        }

        private ApiResult<TopicPartition, Void> handleUnexpectedError(Errors error) {
            log.error("WriteTxnMarkers request for abort spec {} failed due to an unexpected error {}",
                abortSpec, error);
            return ApiResult.failed(abortSpec.topicPartition(), error.exception(
                "WriteTxnMarkers request with " + abortSpec + " failed due to unexpected error: " + error.message()));
        }
}
