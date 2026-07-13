public class kafka_0085 {

        private void handleError(
            CoordinatorKey groupId,
            TopicPartition topicPartition,
            Errors error,
            Map<TopicPartition, Errors> partitionResults,
            Set<CoordinatorKey> groupsToUnmap,
            Set<CoordinatorKey> groupsToRetry
        ) {
            switch (error) {
                // If the coordinator is in the middle of loading, or rebalance is in progress, then we just need to retry.
                case COORDINATOR_LOAD_IN_PROGRESS:
                case REBALANCE_IN_PROGRESS:
                    log.debug("OffsetCommit request for group id {} returned error {}. Will retry.",
                        groupId.idValue, error);
                    groupsToRetry.add(groupId);
                    break;
    
                // If the coordinator is not available, then we unmap and retry.
                case COORDINATOR_NOT_AVAILABLE:
                case NOT_COORDINATOR:
                    log.debug("OffsetCommit request for group id {} returned error {}. Will rediscover the coordinator and retry.",
                        groupId.idValue, error);
                    groupsToUnmap.add(groupId);
                    break;
    
                // Group level errors.
                case INVALID_GROUP_ID:
                case INVALID_COMMIT_OFFSET_SIZE:
                case GROUP_AUTHORIZATION_FAILED:
                case GROUP_ID_NOT_FOUND:
                // Member level errors.
                case UNKNOWN_MEMBER_ID:
                case STALE_MEMBER_EPOCH:
                    log.debug("OffsetCommit request for group id {} failed due to error {}.",
                        groupId.idValue, error);
                    partitionResults.put(topicPartition, error);
                    break;
    
                // TopicPartition level errors.
                case UNKNOWN_TOPIC_OR_PARTITION:
                case OFFSET_METADATA_TOO_LARGE:
                case TOPIC_AUTHORIZATION_FAILED:
                    log.debug("OffsetCommit request for group id {} and partition {} failed due" +
                        " to error {}.", groupId.idValue, topicPartition, error);
                    partitionResults.put(topicPartition, error);
                    break;
    
                // Unexpected errors.
                default:
                    log.error("OffsetCommit request for group id {} and partition {} failed due" +
                        " to unexpected error {}.", groupId.idValue, topicPartition, error);
                    partitionResults.put(topicPartition, error);
            }
        }
}
