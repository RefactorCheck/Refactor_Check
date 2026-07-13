public class kafka_0196 {

    private void balanceTopics(Collection<Uuid> topicIds) {
        List<Uuid> sortedTopicIds = sortTopicIds(topicIds);
        int lastRebalanceTopicIndex = -1;

        MemberAssignmentBalancer memberAssignmentBalancer = new MemberAssignmentBalancer();

        List<Integer> partitions = new ArrayList<>();

        Map<Integer, Integer> startPartitionIndices = new HashMap<>();
        Map<Integer, Integer> endPartitionIndices = new HashMap<>();

        for (int i = 0; i < MAX_ITERATION_COUNT; i++) {
            lastRebalanceTopicIndex = rebalanceTopicsOnce(
                sortedTopicIds,
                lastRebalanceTopicIndex,
                memberAssignmentBalancer,
                partitions,
                startPartitionIndices,
                endPartitionIndices
            );
            if (lastRebalanceTopicIndex == -1) {
                return;
            }
        }
    }

    private int rebalanceTopicsOnce(
        List<Uuid> sortedTopicIds,
        int lastRebalanceTopicIndex,
        MemberAssignmentBalancer memberAssignmentBalancer,
        List<Integer> partitions,
        Map<Integer, Integer> startPartitionIndices,
        Map<Integer, Integer> endPartitionIndices
    ) {
        for (int topicIndex = 0; topicIndex < sortedTopicIds.size(); topicIndex++) {
            if (topicIndex == lastRebalanceTopicIndex) {
                return -1;
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
        return lastRebalanceTopicIndex;
    }
}
