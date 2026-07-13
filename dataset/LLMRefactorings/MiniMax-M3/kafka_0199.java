public class kafka_0199 {

        @Override
        public void onComplete() {
            Map<TopicIdPartition, FetchPartitionData> fetchPartitionData = new LinkedHashMap<>();
            localReadResults.forEach((tpId, result) -> processResult(tpId, result, fetchPartitionData));
    
            responseCallback.accept(fetchPartitionData);
        }

        private void processResult(TopicIdPartition tpId, LogReadResult result, Map<TopicIdPartition, FetchPartitionData> fetchPartitionData) {
            CompletableFuture<RemoteLogReadResult> remoteFetchResult = remoteFetchResults.get(tpId);
            if (remoteFetchResults.containsKey(tpId)
                && remoteFetchResult.isDone()
                && result.error() == Errors.NONE
                && result.info().delayedRemoteStorageFetch.isPresent()) {
    
                if (remoteFetchResult.join().error().isPresent()) {
                    fetchPartitionData.put(tpId,
                        new LogReadResult(Errors.forException(remoteFetchResult.join().error().get())).toFetchPartitionData(false));
                } else {
                    FetchDataInfo info = remoteFetchResult.join().fetchDataInfo().get();
                    fetchPartitionData.put(tpId,
                        new FetchPartitionData(
                            result.error(),
                            result.highWatermark(),
                            result.leaderLogStartOffset(),
                            info.records,
                            Optional.empty(),
                            result.lastStableOffset(),
                            info.abortedTransactions,
                            result.preferredReadReplica(),
                            false));
                }
            } else {
                fetchPartitionData.put(tpId, result.toFetchPartitionData(false));
            }
        }
}
