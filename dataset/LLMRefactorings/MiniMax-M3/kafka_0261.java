public class kafka_0261 {

        private CompletableFuture<Void> updatePositionsWithOffsets(long deadlineMs) {
            CompletableFuture<Void> result = new CompletableFuture<>();
    
            cacheExceptionIfEventExpired(result, deadlineMs);
    
            CompletableFuture<Void> updatePositions;
            final Set<TopicPartition> initializingPartitions = subscriptionState.initializingPartitions();
            if (commitRequestManager != null) {
                CompletableFuture<Void> refreshWithCommittedOffsets = initWithCommittedOffsetsIfNeeded(initializingPartitions, deadlineMs);
    
                updatePositions = refreshWithCommittedOffsets.thenCompose(__ -> initWithPartitionOffsetsIfNeeded(initializingPartitions));
    
            } else {
                updatePositions = initWithPartitionOffsetsIfNeeded(initializingPartitions);
            }
    
            updatePositions.whenComplete((__, resetError) -> completeResult(result, resetError));
    
            return result;
        }
    
        private void completeResult(CompletableFuture<Void> result, Throwable resetError) {
            if (resetError == null) {
                result.complete(null);
            } else {
                result.completeExceptionally(resetError);
            }
        }
}
