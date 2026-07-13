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
                case COORDINATOR_LOAD_IN_PROGRESS:
                case REBALANCE_IN_PROGRESS:
                    retryOffsetCommit(groupId, error, groupsToRetry);
                    break;

                case COORDINATOR_NOT_AVAILABLE:
                case NOT_COORDINATOR:
                    rediscoverCoordinator(groupId, error, groupsToUnmap);
                    break;

                case INVALID_GROUP_ID:
                case INVALID_COMMIT_OFFSET_SIZE:
                case GROUP_AUTHORIZATION_FAILED:
                case GROUP_ID_NOT_FOUND:
                case UNKNOWN_MEMBER_ID:
                case STALE_MEMBER_EPOCH:
                    failGroupCommit(groupId, topicPartition, error, partitionResults);
                    break;

                case UNKNOWN_TOPIC_OR_PARTITION:
                case OFFSET_METADATA_TOO_LARGE:
                case TOPIC_AUTHORIZATION_FAILED:
                    failPartitionCommit(groupId, topicPartition, error, partitionResults);
                    break;

                default:
                    failUnexpectedCommit(groupId, topicPartition, error, partitionResults);
            }
        }

        private void retryOffsetCommit(CoordinatorKey groupId, Errors error, Set<CoordinatorKey> groupsToRetry) {
            log.debug("OffsetCommit request for group id {} returned error {}. Will retry.",
                groupId.idValue, error);
            groupsToRetry.add(groupId);
        }

        private void rediscoverCoordinator(CoordinatorKey groupId, Errors error, Set<CoordinatorKey> groupsToUnmap) {
            log.debug("OffsetCommit request for group id {} returned error {}. Will rediscover the coordinator and retry.",
                groupId.idValue, error);
            groupsToUnmap.add(groupId);
        }

        private void failGroupCommit(CoordinatorKey groupId,
                                     TopicPartition topicPartition,
                                     Errors error,
                                     Map<TopicPartition, Errors> partitionResults) {
            log.debug("OffsetCommit request for group id {} failed due to error {}.",
                groupId.idValue, error);
            partitionResults.put(topicPartition, error);
        }

        private void failPartitionCommit(CoordinatorKey groupId,
                                         TopicPartition topicPartition,
                                         Errors error,
                                         Map<TopicPartition, Errors> partitionResults) {
            log.debug("OffsetCommit request for group id {} and partition {} failed due" +
                " to error {}.", groupId.idValue, topicPartition, error);
            partitionResults.put(topicPartition, error);
        }

        private void failUnexpectedCommit(CoordinatorKey groupId,
                                          TopicPartition topicPartition,
                                          Errors error,
                                          Map<TopicPartition, Errors> partitionResults) {
            log.error("OffsetCommit request for group id {} and partition {} failed due" +
                " to unexpected error {}.", groupId.idValue, topicPartition, error);
            partitionResults.put(topicPartition, error);
        }
}
