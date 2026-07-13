public class kafka_0103 {

        private GroupAssignment assignHeterogeneousGroupRefactored(
            GroupSpec groupSpec,
            SubscribedTopicDescriber subscribedTopicDescriber
        ) throws PartitionAssignorException {
            List<String> memberIds = sortMemberIds(groupSpec);
    
            Map<Uuid, TopicMetadata> topics = new HashMap<>();
    
            for (String memberId : memberIds) {
                MemberSubscription subs = groupSpec.memberSubscription(memberId);
                for (Uuid topicId : subs.subscribedTopicIds()) {
                    TopicMetadata topicMetadata = topics.computeIfAbsent(topicId, __ -> {
                        int numPartitions = subscribedTopicDescriber.numPartitions(topicId);
                        if (numPartitions == -1) {
                            throw new PartitionAssignorException("Member is subscribed to a non-existent topic");
                        }
    
                        return new TopicMetadata(
                            topicId,
                            numPartitions,
                            0
                        );
                    });
                    topicMetadata.numMembers++;
                }
            }
    
            Map<String, MemberAssignment> assignments = new HashMap<>((int) ((groupSpec.memberIds().size() / 0.75f) + 1));
    
            for (String memberId : memberIds) {
                MemberSubscription subs = groupSpec.memberSubscription(memberId);
                Map<Uuid, Set<Integer>> assignment = new HashMap<>((int) ((subs.subscribedTopicIds().size() / 0.75f) + 1));
                for (Uuid topicId : subs.subscribedTopicIds()) {
                    TopicMetadata metadata = topics.get(topicId);
                    metadata.maybeComputeQuota();
                    addPartitionsToAssignment(metadata, assignment);
                }
                assignments.put(memberId, new MemberAssignmentImpl(assignment));
            }
    
            return new GroupAssignment(assignments);
        }
}
