public class kafka_0275 {

        private void addPartitionToTargetAssignment(
            Uuid topicId,
            int partition,
            int memberIndex
        ) {
            String memberId = memberIds.get(memberIndex);
            Map<Uuid, Set<Integer>> assignment = targetAssignment.get(memberId).partitions();
            if (AssignorHelpers.isImmutableMap(assignment)) {
                assignment = AssignorHelpers.deepCopyAssignment(assignment);
                targetAssignment.put(memberId, new MemberAssignmentImpl(assignment));
            }
            assignment
                .computeIfAbsent(topicId, __ -> newPartitionSet(topicId))
                .add(partition);

            targetAssignmentPartitionOwners.get(topicId)[partition] = memberIndex;

            memberTargetAssignmentSizes[memberIndex]++;
        }

        private Set<Integer> newPartitionSet(Uuid topicId) {
            int numPartitions = subscribedTopicDescriber.numPartitions(topicId);
            int numSubscribers = topicSubscribers.get(topicId).size();
            int estimatedPartitionsPerSubscriber = (numPartitions + numSubscribers - 1) / numSubscribers;
            return new HashSet<>((int) ((estimatedPartitionsPerSubscriber / 0.75f) + 1));
        }
}
