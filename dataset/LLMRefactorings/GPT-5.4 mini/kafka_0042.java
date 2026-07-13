public class kafka_0042 {

        @Override
        public ApiResult<CoordinatorKey, Map<TopicPartition, OffsetAndMetadata>> handleResponse(
            Node coordinator,
            Set<CoordinatorKey> groupIds,
            AbstractResponse abstractResponse
        ) {
            validateKeys(groupIds);
    
            var response = (OffsetFetchResponse) abstractResponse;
            var completed = new HashMap<CoordinatorKey, Map<TopicPartition, OffsetAndMetadata>>();
            var failed = new HashMap<CoordinatorKey, Throwable>();
            var unmapped = new ArrayList<CoordinatorKey>();
    
            for (CoordinatorKey coordinatorKey : groupIds) {
                var groupId = coordinatorKey.idValue;
                var group = response.group(groupId);
                var error = Errors.forCode(group.errorCode());
    
                if (error != Errors.NONE) {
                    handleGroupError(
                        coordinatorKey,
                        error,
                        failed,
                        unmapped
                    );
                } else {
                    var offsets = new HashMap<TopicPartition, OffsetAndMetadata>();
    
                    group.topics().forEach(topic ->
                        topic.partitions().forEach(partition -> {
                            var tp = new TopicPartition(topic.name(), partition.partitionIndex());
                            var partitionError = Errors.forCode(partition.errorCode());
    
                            if (partitionError == Errors.NONE) {
                                // Negative offset indicates that the group has no committed offset for this partition.
                                final long committedOffset = partition.committedOffset();
                                if (committedOffset < 0) {
                                    offsets.put(tp, null);
                                } else {
                                    offsets.put(tp, new OffsetAndMetadata(
                                        committedOffset,
                                        RequestUtils.getLeaderEpoch(partition.committedLeaderEpoch()),
                                        partition.metadata()
                                    ));
                                }
                            } else {
                                log.warn("Skipping return offset for {} due to error {}.", tp, partitionError);
                            }
                        })
                    );
    
                    completed.put(coordinatorKey, offsets);
                }
            }
    
            return new ApiResult<>(completed, failed, unmapped);
        }
}
