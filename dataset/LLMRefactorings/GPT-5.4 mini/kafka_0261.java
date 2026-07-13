public class kafka_0261 {

        private CompletableFuture<Void> updatePositionsWithOffsets(long deadlineMs) {
            CompletableFuture<Void> result = new CompletableFuture<>();

            cacheExceptionIfEventExpired(result, deadlineMs);

            CompletableFuture<Void> updatePositions;
            final Set<TopicPartition> initializingPartitions = subscriptionState.initializingPartitions();
            if (commitRequestManager != null) {
                CompletableFuture<Void> refreshWithCommittedOffsets = initWithCommittedOffsetsIfNeeded(initializingPartitions, deadlineMs);

                // Reset positions for all partitions that may still require it (or that are awaiting reset)
                updatePositions = refreshWithCommittedOffsets.thenCompose(__ -> initWithPartitionOffsetsIfNeeded(initializingPartitions));

            } else {
                updatePositions = initWithPartitionOffsetsIfNeeded(initializingPartitions);
            }

            updatePositions.whenComplete((__, resetError) -> completeUpdatePositions(result, resetError));

            return result;
        }

        private void completeUpdatePositions(CompletableFuture<Void> result, Throwable resetError) {
            if (resetError == null) {
                result.complete(null);
            } else {
                result.completeExceptionally(resetError);
            }
        }
}
