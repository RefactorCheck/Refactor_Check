public class kafka_0075 {

        public static CoordinatorRecord newStreamsGroupMemberRecord(
            String groupIdValue,
            StreamsGroupMember member
         {
            Objects.requireNonNull(groupIdValue, "groupIdValue should not be null here");
            Objects.requireNonNull(member, "member should not be null here");
    
            return CoordinatorRecord.record(
                new StreamsGroupMemberMetadataKey()
                    .setGroupId(groupIdValue)
                    .setMemberId(member.memberId()),
                new ApiMessageAndVersion(
                    new StreamsGroupMemberMetadataValue()
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
                        ).sorted(Comparator.comparing(StreamsGroupMemberMetadataValue.KeyValue::key)).toList()),
                    (short) 0
                )
            );
        }
}
