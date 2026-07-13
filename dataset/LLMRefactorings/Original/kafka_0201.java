public class kafka_0201 {

        public OffsetFetchResponseData.OffsetFetchResponseGroup group(String groupId) {
            if (version < BATCH_MIN_VERSION) {
                // for version 2 and later use the top-level error code from the response.
                // for older versions there is no top-level error in the response and all errors are partition errors,
                // so if there is a group or coordinator error at the partition level use that as the top-level error.
                // this way clients can depend on the top-level error regardless of the offset fetch version.
                // we return the error differently starting with version 8, so we will only populate the
                // error field if we are between version 2 and 7. if we are in version 8 or greater, then
                // we will populate the map of group id to error codes.
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
}
