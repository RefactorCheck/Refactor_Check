public class kafka_0292 {

        @Override
        public OffsetFetchResponse getErrorResponse(int throttleTimeMs, Throwable e) {
            Errors error = Errors.forException(e);
    
            if (version() < TOP_LEVEL_ERROR_AND_NULL_TOPICS_MIN_VERSION) {
                // The response does not support top level error so we return each
                // partition with the error.
                return new OffsetFetchResponse(
                    new OffsetFetchResponseData()
                        .setThrottleTimeMs(throttleTimeMs)
                        .setTopics(data.topics().stream().map(topic ->
                            new OffsetFetchResponseData.OffsetFetchResponseTopic()
                                .setName(topic.name())
                                .setPartitions(topic.partitionIndexes().stream().map(partition ->
                                    new OffsetFetchResponseData.OffsetFetchResponsePartition()
                                        .setPartitionIndex(partition)
                                        .setErrorCode(error.code())
                                        .setCommittedOffset(OffsetFetchResponse.INVALID_OFFSET)
                                        .setMetadata(OffsetFetchResponse.NO_METADATA)
                                        .setCommittedLeaderEpoch(RecordBatch.NO_PARTITION_LEADER_EPOCH)
                                ).collect(Collectors.toList()))
                        ).collect(Collectors.toList())),
                    version()
                );
            } else if (version() < BATCH_MIN_VERSION) {
                // The response does not support multiple groups but it does support
                // top level error.
                return new OffsetFetchResponse(
                    new OffsetFetchResponseData()
                        .setThrottleTimeMs(throttleTimeMs)
                        .setErrorCode(error.code()),
                    version()
                );
            } else {
                // The response does support multiple groups so we provide a top level
                // error per group.
                return new OffsetFetchResponse(
                    new OffsetFetchResponseData()
                        .setThrottleTimeMs(throttleTimeMs)
                        .setGroups(data.groups().stream().map(group ->
                            new OffsetFetchResponseData.OffsetFetchResponseGroup()
                                .setGroupId(group.groupId())
                                .setErrorCode(error.code())
                        ).collect(Collectors.toList())),
                    version()
                );
            }
        }
}
