public class kafka_0196 {

        private void balanceTopics(Collection<Uuid> topicIds) {
            List<Uuid> sortedTopicIds = sortTopicIds(topicIds);
            // The index of the last topic in sortedTopicIds that was rebalanced.
            // Used to decide when to exit early.
            int lastRebalanceTopicIndex = -1;
    
            MemberAssignmentBalancer memberAssignmentBalancer = new MemberAssignmentBalancer();
    
            // An array of partitions, with partitions owned by the same member grouped together.
            List<Integer> partitions = new ArrayList<>();
    
            // The ranges in the partitions list assigned to members.
            // Maintaining these ranges allows for constant time, deterministic picking of partitions
            // owned by a given member.
            Map<Integer, Integer> startPartitionIndices = new HashMap<>();
            Map<Integer, Integer> endPartitionIndices = new HashMap<>(); // exclusive
    
            // Repeat reassignment until no partition can be moved to improve the balance or we hit an
            // iteration limit.
            for (int i = 0; i < MAX_ITERATION_COUNT; i++) {
                for (int topicIndex = 0; topicIndex < sortedTopicIds.size(); topicIndex++) {
                    if (topicIndex == lastRebalanceTopicIndex) {
                        // The last rebalanced topic was this one, which means we've gone through all
                        // the topics and didn't perform any additional rebalancing. Don't bother trying
                        // to rebalance this topic again and exit.
                        return;
                    }
    
                    Uuid topicId = sortedTopicIds.get(topicIndex);
    
                    int reassignedPartitionCount = balanceTopic(
                        topicId,
                        memberAssignmentBalancer,
                        partitions,
                        startPartitionIndices,
                        endPartitionIndices
                    );
    
                    if (reassignedPartitionCount > 0 ||
                        lastRebalanceTopicIndex == -1) {
                        lastRebalanceTopicIndex = topicIndex;
                    }
                }
            }
        }
}
