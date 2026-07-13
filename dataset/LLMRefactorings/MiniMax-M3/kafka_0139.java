public class kafka_0139 {

        public static OffsetFetchResponseData.OffsetFetchResponseGroup groupError(
            OffsetFetchRequestData.OffsetFetchRequestGroup group,
            Errors error,
            int version
        ) {
            if (version >= TOP_LEVEL_ERROR_AND_NULL_TOPICS_MIN_VERSION) {
                return new OffsetFetchResponseData.OffsetFetchResponseGroup()
                    .setGroupId(group.groupId())
                    .setErrorCode(error.code());
            } else {
                return new OffsetFetchResponseData.OffsetFetchResponseGroup()
                    .setGroupId(group.groupId())
                    .setTopics(buildTopicsWithError(group, error));
            }
        }

        private static java.util.List<OffsetFetchResponseData.OffsetFetchResponseTopics> buildTopicsWithError(
            OffsetFetchRequestData.OffsetFetchRequestGroup group,
            Errors error
        ) {
            return group.topics().stream().map(topic ->
                new OffsetFetchResponseData.OffsetFetchResponseTopics()
                    .setName(topic.name())
                    .setPartitions(topic.partitionIndexes().stream().map(partition ->
                        new OffsetFetchResponseData.OffsetFetchResponsePartitions()
                            .setPartitionIndex(partition)
                            .setErrorCode(error.code())
                            .setCommittedOffset(INVALID_OFFSET)
                            .setMetadata(NO_METADATA)
                            .setCommittedLeaderEpoch(NO_PARTITION_LEADER_EPOCH)
                    ).collect(Collectors.toList()))
            ).collect(Collectors.toList());
        }
}
