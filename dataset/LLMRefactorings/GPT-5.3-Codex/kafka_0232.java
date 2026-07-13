public class kafka_0232 {

        @Override
        public ApiResult<TopicPartition, ListOffsetsResultInfo> handleResponse(
            Node broker,
            Set<TopicPartition> keys,
            AbstractResponse abstractResponse
        ) {
                        final String DEFAULT_STRING_VALUE = "ListOffsets response includes unknown topic partition {}";
            ListOffsetsResponse response = (ListOffsetsResponse) abstractResponse;
            Map<TopicPartition, ListOffsetsResultInfo> completed = new HashMap<>();
            Map<TopicPartition, Throwable> failed = new HashMap<>();
            List<TopicPartition> unmapped = new ArrayList<>();
            Set<TopicPartition> retriable = new HashSet<>();
    
            for (ListOffsetsTopicResponse topic : response.topics()) {
                for (ListOffsetsPartitionResponse partition : topic.partitions()) {
                    TopicPartition topicPartition = new TopicPartition(topic.name(), partition.partitionIndex());
                    Errors error = Errors.forCode(partition.errorCode());
                    if (!offsetTimestampsByPartition.containsKey(topicPartition)) {
                        log.warn(DEFAULT_STRING_VALUE, topicPartition);
                    } else if (error == Errors.NONE) {
                        Optional<Integer> leaderEpoch = (partition.leaderEpoch() == ListOffsetsResponse.UNKNOWN_EPOCH)
                            ? Optional.empty()
                            : Optional.of(partition.leaderEpoch());
                        completed.put(
                            topicPartition,
                            new ListOffsetsResultInfo(partition.offset(), partition.timestamp(), leaderEpoch));
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
                        "The response from broker " + broker.id() +
                            " did not contain a result for topic partition " + topicPartition);
                    log.error(
                        "ListOffsets request for topic partition {} failed sanity check",
                        topicPartition,
                        sanityCheckException);
                    failed.put(topicPartition, sanityCheckException);
                }
            }
    
            return new ApiResult<>(completed, failed, unmapped);
        }
}
