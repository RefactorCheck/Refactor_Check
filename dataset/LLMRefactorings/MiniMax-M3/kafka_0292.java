public class kafka_0292 {

        @Override
        public OffsetFetchResponse getErrorResponse(int throttleTimeMs, Throwable e) {
            Errors error = Errors.forException(e);
            short errorCode = error.code();
    
            if (version() < TOP_LEVEL_ERROR_AND_NULL_TOPICS_MIN_VERSION) {
                return new OffsetFetchResponse(
                    new OffsetFetchResponseData()
                        .setThrottleTimeMs(throttleTimeMs)
                        .setTopics(data.topics().stream().map(topic ->
                            new OffsetFetchResponseData.OffsetFetchResponseTopic()
                                .setName(topic.name())
                                .setPartitions(topic.partitionIndexes().stream().map(partition ->
                                    new OffsetFetchResponseData.OffsetFetchResponsePartition()
                                        .setPartitionIndex(partition)
                                        .setErrorCode(errorCode)
                                        .setCommittedOffset(OffsetFetchResponse.INVALID_OFFSET)
                                        .setMetadata(OffsetFetchResponse.NO_METADATA)
                                        .setCommittedLeaderEpoch(RecordBatch.NO_PARTITION_LEADER_EPOCH)
                                ).collect(Collectors.toList()))
                        ).collect(Collectors.toList())),
                    version()
                );
            } else if (version() < BATCH_MIN_VERSION) {
                return new OffsetFetchResponse(
                    new OffsetFetchResponseData()
                        .setThrottleTimeMs(throttleTimeMs)
                        .setErrorCode(errorCode),
                    version()
                );
            } else {
                return new OffsetFetchResponse(
                    new OffsetFetchResponseData()
                        .setThrottleTimeMs(throttleTimeMs)
                        .setGroups(data.groups().stream().map(group ->
                            new OffsetFetchResponseData.OffsetFetchResponseGroup()
                                .setGroupId(group.groupId())
                                .setErrorCode(errorCode)
                        ).collect(Collectors.toList())),
                    version()
                );
            }
        }
}
