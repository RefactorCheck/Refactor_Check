public class kafka_0275 {

        private void addPartitionToTargetAssignment(
            Uuid topicId,
            int partition,
            int memberIndex
        ) {
            String assignedMemberId = memberIds.get(memberIndex);
            Map<Uuid, Set<Integer>> assignment = targetAssignment.get(assignedMemberId).partitions();
            if (AssignorHelpers.isImmutableMap(assignment)) {
                assignment = AssignorHelpers.deepCopyAssignment(assignment);
                targetAssignment.put(assignedMemberId, new MemberAssignmentImpl(assignment));
            }
            assignment
                .computeIfAbsent(topicId, __ -> {
                    int numPartitions = subscribedTopicDescriber.numPartitions(topicId);
                    int numSubscribers = topicSubscribers.get(topicId).size();
                    int estimatedPartitionsPerSubscriber = (numPartitions + numSubscribers - 1) / numSubscribers;
                    return new HashSet<>((int) ((estimatedPartitionsPerSubscriber / 0.75f) + 1));
                })
                .add(partition);

            targetAssignmentPartitionOwners.get(topicId)[partition] = memberIndex;

            memberTargetAssignmentSizes[memberIndex]++;
        }
}
