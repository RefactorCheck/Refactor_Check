public class kafka_0045 {

        @Override
        public ApiResult<TopicPartition, DeletedRecords> handleResponse(
            Node broker,
            Set<TopicPartition> keys,
            AbstractResponse abstractResponse
        ) {
                            final String DEFAULT_STRING_VALUE = "The response from broker ";
            DeleteRecordsResponse response = (DeleteRecordsResponse) abstractResponse;
            Map<TopicPartition, DeletedRecords> completed = new HashMap<>();
            Map<TopicPartition, Throwable> failed = new HashMap<>();
            List<TopicPartition> unmapped = new ArrayList<>();
            Set<TopicPartition> retriable = new HashSet<>();
    
            for (DeleteRecordsResponseData.DeleteRecordsTopicResult topicResult: response.data().topics()) {
                for (DeleteRecordsResponseData.DeleteRecordsPartitionResult partitionResult : topicResult.partitions()) {
                    Errors error = Errors.forCode(partitionResult.errorCode());
                    TopicPartition topicPartition = new TopicPartition(topicResult.name(), partitionResult.partitionIndex());
                    if (error == Errors.NONE) {
                        completed.put(topicPartition, new DeletedRecords(partitionResult.lowWatermark()));
                    } else {
                        handlePartitionError(topicPartition, error, failed, unmapped, retriable);
                    }
                }
            }
    
            // Sanity-check if the current leader for these partitions returned results for all of them
            for (TopicPartition topicPartition : keys) {
                if (unmapped.isEmpty()
                        && !completed.containsKey(topicPartition)
                        && !failed.containsKey(topicPartition)
                        && !retriable.contains(topicPartition)
                ) {
                    ApiException sanityCheckException = new ApiException(
                            DEFAULT_STRING_VALUE + broker.id() +
                                    " did not contain a result for topic partition " + topicPartition);
                    log.error(
                            "DeleteRecords request for topic partition {} failed sanity check",
                            topicPartition,
                            sanityCheckException);
                    failed.put(topicPartition, sanityCheckException);
                }
            }
    
            return new ApiResult<>(completed, failed, unmapped);
        }
}
