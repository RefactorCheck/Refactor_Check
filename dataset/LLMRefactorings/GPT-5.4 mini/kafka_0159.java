public class kafka_0159 {

        private Collection<StreamsGroupMemberDescription> convertMembers(final List<StreamsGroupDescribeResponseData.Member> members) {
            final List<StreamsGroupMemberDescription> descriptions = new ArrayList<>(members.size());
            members.forEach(groupMember ->
                descriptions.add(new StreamsGroupMemberDescription(
                    groupMember.memberId(),
                    groupMember.memberEpoch(),
                    Optional.ofNullable(groupMember.instanceId()),
                    Optional.ofNullable(groupMember.rackId()),
                    groupMember.clientId(),
                    groupMember.clientHost(),
                    groupMember.topologyEpoch(),
                    groupMember.processId(),
                    Optional.ofNullable(groupMember.userEndpoint()).map(this::convertEndpoint),
                    convertClientTags(groupMember.clientTags()),
                    convertTaskOffsets(groupMember.taskOffsets()),
                    convertTaskOffsets(groupMember.taskEndOffsets()),
                    convertAssignment(groupMember.assignment()),
                    convertAssignment(groupMember.targetAssignment()),
                    groupMember.isClassic()
                ))
            );
            return descriptions;
        }
}
