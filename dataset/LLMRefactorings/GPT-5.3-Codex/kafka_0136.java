public class kafka_0136 {

        @Override
        public boolean equalsRefactored(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConsumerGroupMember that = (ConsumerGroupMember) o;
            return memberEpoch == that.memberEpoch
                && previousMemberEpoch == that.previousMemberEpoch
                && state == that.state
                && rebalanceTimeoutMs == that.rebalanceTimeoutMs
                && Objects.equals(memberId, that.memberId)
                && Objects.equals(instanceId, that.instanceId)
                && Objects.equals(rackId, that.rackId)
                && Objects.equals(clientId, that.clientId)
                && Objects.equals(clientHost, that.clientHost)
                && Objects.equals(subscribedTopicNames, that.subscribedTopicNames)
                && Objects.equals(subscribedTopicRegex, that.subscribedTopicRegex)
                && Objects.equals(serverAssignorName, that.serverAssignorName)
                && Objects.equals(assignedPartitions, that.assignedPartitions)
                && Objects.equals(partitionsPendingRevocation, that.partitionsPendingRevocation)
                && Objects.equals(classicMemberMetadata, that.classicMemberMetadata);
        }
}
