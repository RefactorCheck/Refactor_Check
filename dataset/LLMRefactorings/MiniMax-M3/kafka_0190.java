public class kafka_0190 {

        public static CoordinatorRecord newGroupMetadataRecord(
            ClassicGroup group,
            Map<String, byte[]> assignment
        ) {
            List<GroupMetadataValue.MemberMetadata> members = new ArrayList<>(group.allMembers().size());
            group.allMembers().forEach(member -> members.add(buildMemberMetadata(group, assignment, member)));
    
            return CoordinatorRecord.record(
                new GroupMetadataKey()
                    .setGroup(group.groupId()),
                new ApiMessageAndVersion(
                    new GroupMetadataValue()
                        .setProtocol(group.protocolName().orElse(null))
                        .setProtocolType(group.protocolType().orElse(""))
                        .setGeneration(group.generationId())
                        .setLeader(group.leaderOrNull())
                        .setCurrentStateTimestamp(group.currentStateTimestampOrDefault())
                        .setMembers(members),
                    GROUP_METADATA_VALUE_VERSION
                )
            );
        }
    
        private static GroupMetadataValue.MemberMetadata buildMemberMetadata(
            ClassicGroup group,
            Map<String, byte[]> assignment,
            ClassicGroup.Member member
        ) {
            byte[] subscription = group.protocolName().map(member::metadata).orElse(null);
            if (subscription == null) {
                throw new IllegalStateException("Attempted to write non-empty group metadata with no defined protocol.");
            }
    
            byte[] memberAssignment = assignment.get(member.memberId());
            if (memberAssignment == null) {
                throw new IllegalStateException("Attempted to write member " + member.memberId() +
                    " of group " + group.groupId() + " with no assignment.");
            }
    
            return new GroupMetadataValue.MemberMetadata()
                .setMemberId(member.memberId())
                .setClientId(member.clientId())
                .setClientHost(member.clientHost())
                .setRebalanceTimeout(member.rebalanceTimeoutMs())
                .setSessionTimeout(member.sessionTimeoutMs())
                .setGroupInstanceId(member.groupInstanceId().orElse(null))
                .setSubscription(subscription)
                .setAssignment(memberAssignment);
        }
}
