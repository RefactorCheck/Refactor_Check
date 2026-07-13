public class kafka_0103 {

        private GroupAssignment assignHeterogeneousGroup(
            GroupSpec groupSpec,
            SubscribedTopicDescriber subscribedTopicDescriber
        ) throws PartitionAssignorException {
            List<String> memberIds = sortMemberIds(groupSpec);
    
            Map<Uuid, TopicMetadata> topics = buildTopicMetadata(groupSpec, memberIds, subscribedTopicDescriber);
    
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
    
        private Map<Uuid, TopicMetadata> buildTopicMetadata(
            GroupSpec groupSpec,
            List<String> memberIds,
            SubscribedTopicDescriber subscribedTopicDescriber
        ) throws PartitionAssignorException {
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
    
            return topics;
        }
}
