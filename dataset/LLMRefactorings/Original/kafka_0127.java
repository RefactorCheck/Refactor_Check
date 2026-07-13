public class kafka_0127 {

        private void fetchOffsetsWithRetries(final OffsetFetchRequestState fetchRequest,
                                             final CompletableFuture<OffsetFetchResult> result) {
            CompletableFuture<OffsetFetchResult> currentResult = pendingRequests.addOffsetFetchRequest(fetchRequest);
    
            // Retry the same fetch request while it fails with RetriableException and the retry timeout hasn't expired.
            currentResult.whenComplete((res, error) -> {
                boolean inflightRemoved = pendingRequests.inflightOffsetFetches.remove(fetchRequest);
                if (!inflightRemoved) {
                    log.warn("A duplicated, inflight, request was identified, but unable to find it in the " +
                        "outbound buffer: {}", fetchRequest);
                }
    
                // Group-level error
                if (error != null) {
                    handleGroupLevelError(fetchRequest, result, error);
                    return;
                }
    
                // Partition-level errors
                if (res.hasRetriablePartitionErrors()) {
                    handleRetriablePartitionErrors(fetchRequest, result, res);
                    return;
                }
    
                handleSuccessfulOffsetFetch(result, res);
    
            });
        }
}
