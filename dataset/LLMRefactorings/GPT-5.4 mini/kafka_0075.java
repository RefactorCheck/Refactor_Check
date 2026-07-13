public class kafka_0075 {

        public static CoordinatorRecord newStreamsGroupMemberRecord(
            String groupId,
            StreamsGroupMember member
        ) {
            Objects.requireNonNull(groupId, "groupId should not be null here");
            Objects.requireNonNull(member, "member should not be null here");

            StreamsGroupMemberMetadataValue metadataValue = new StreamsGroupMemberMetadataValue()
                .setRackId(member.rackId().orElse(null))
                .setInstanceId(member.instanceId().orElse(null))
                .setClientId(member.clientId())
                .setClientHost(member.clientHost())
                .setRebalanceTimeoutMs(member.rebalanceTimeoutMs())
                .setTopologyEpoch(member.topologyEpoch())
                .setProcessId(member.processId())
                .setUserEndpoint(member.userEndpoint().orElse(null))
                .setClientTags(member.clientTags().entrySet().stream().map(e ->
                    new StreamsGroupMemberMetadataValue.KeyValue()
                        .setKey(e.getKey())
                        .setValue(e.getValue())
                ).sorted(Comparator.comparing(StreamsGroupMemberMetadataValue.KeyValue::key)).toList());

            return CoordinatorRecord.record(
                new StreamsGroupMemberMetadataKey()
                    .setGroupId(groupId)
                    .setMemberId(member.memberId()),
                new ApiMessageAndVersion(
                    metadataValue,
                    (short) 0
                )
            );
        }
}
