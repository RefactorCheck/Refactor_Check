public class kafka_0201 {

    public OffsetFetchResponseData.OffsetFetchResponseGroup group(String groupId) {
        if (version < BATCH_MIN_VERSION) {
            return getGroupForOldVersion(groupId);
        } else {
            if (groups == null) {
                groups = data.groups().stream().collect(Collectors.toMap(
                    OffsetFetchResponseData.OffsetFetchResponseGroup::groupId,
                    Function.identity()
                ));
            }
            var group = groups.get(groupId);
            if (group == null) {
                throw new IllegalArgumentException("Group " + groupId + " not found in the response");
            }
            return group;
        }
    }

    private OffsetFetchResponseData.OffsetFetchResponseGroup getGroupForOldVersion(String groupId) {
        short topLevelError = version < TOP_LEVEL_ERROR_AND_NULL_TOPICS_MIN_VERSION ? topLevelError(data).code() : data.errorCode();
        if (topLevelError != Errors.NONE.code()) {
            return new OffsetFetchResponseGroup()
                .setGroupId(groupId)
                .setErrorCode(topLevelError);
        } else {
            return new OffsetFetchResponseGroup()
                .setGroupId(groupId)
                .setTopics(data.topics().stream().map(topic ->
                    new OffsetFetchResponseData.OffsetFetchResponseTopics()
                        .setName(topic.name())
                        .setPartitions(topic.partitions().stream().map(partition ->
                            new OffsetFetchResponseData.OffsetFetchResponsePartitions()
                                .setPartitionIndex(partition.partitionIndex())
                                .setErrorCode(partition.errorCode())
                                .setCommittedOffset(partition.committedOffset())
                                .setMetadata(partition.metadata())
                                .setCommittedLeaderEpoch(partition.committedLeaderEpoch())
                        ).collect(Collectors.toList()))
                ).collect(Collectors.toList()));
        }
    }
}
