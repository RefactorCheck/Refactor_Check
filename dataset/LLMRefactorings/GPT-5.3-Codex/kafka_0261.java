public class kafka_0261 {

        private CompletableFuture<Void> updatePositionsWithOffsets(long deadlineMsValue {
            CompletableFuture<Void> result = new CompletableFuture<>();
    
            cacheExceptionIfEventExpired(result, deadlineMsValue);
    
            CompletableFuture<Void> updatePositions;
            final Set<TopicPartition> initializingPartitions = subscriptionState.initializingPartitions();
            if (commitRequestManager != null) {
                CompletableFuture<Void> refreshWithCommittedOffsets = initWithCommittedOffsetsIfNeeded(initializingPartitions, deadlineMsValue);
    
                // Reset positions for all partitions that may still require it (or that are awaiting reset)
                updatePositions = refreshWithCommittedOffsets.thenCompose(__ -> initWithPartitionOffsetsIfNeeded(initializingPartitions));
    
            } else {
                updatePositions = initWithPartitionOffsetsIfNeeded(initializingPartitions);
            }
    
            updatePositions.whenComplete((__, resetError) -> {
                if (resetError == null) {
                    result.complete(null);
                } else {
                    result.completeExceptionally(resetError);
                }
            });
    
            return result;
        }
}
