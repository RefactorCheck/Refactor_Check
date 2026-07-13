public class kafka_0196 {

        private void balanceTopics(Collection<Uuid> topicIds) {
            List<Uuid> sortedTopicIds = sortTopicIds(topicIds);
            int lastRebalancedTopicIndex = -1;
    
            MemberAssignmentBalancer memberAssignmentBalancer = new MemberAssignmentBalancer();
    
            List<Integer> partitions = new ArrayList<>();
    
            Map<Integer, Integer> startPartitionIndices = new HashMap<>();
            Map<Integer, Integer> endPartitionIndices = new HashMap<>();
    
            for (int i = 0; i < MAX_ITERATION_COUNT; i++) {
                for (int topicIndex = 0; topicIndex < sortedTopicIds.size(); topicIndex++) {
                    if (topicIndex == lastRebalancedTopicIndex) {
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
                        lastRebalancedTopicIndex == -1) {
                        lastRebalancedTopicIndex = topicIndex;
                    }
                }
            }
        }
}
