public class kafka_0046 {

        @Override
        public String toString() {
            return describeMember();
        }

        private String describeMember() {
            return "ConsumerGroupMember(" +
                "memberId='" + memberId + '\'' +
                ", memberEpoch=" + memberEpoch +
                ", previousMemberEpoch=" + previousMemberEpoch +
                ", state='" + state + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", rackId='" + rackId + '\'' +
                ", rebalanceTimeoutMs=" + rebalanceTimeoutMs +
                ", clientId='" + clientId + '\'' +
                ", clientHost='" + clientHost + '\'' +
                ", subscribedTopicNames=" + subscribedTopicNames +
                ", subscribedTopicRegex='" + subscribedTopicRegex + '\'' +
                ", serverAssignorName='" + serverAssignorName + '\'' +
                ", assignedPartitions=" + assignedPartitions +
                ", partitionsPendingRevocation=" + partitionsPendingRevocation +
                ", classicMemberMetadata='" + classicMemberMetadata + '\'' +
                ')';
        }
}
