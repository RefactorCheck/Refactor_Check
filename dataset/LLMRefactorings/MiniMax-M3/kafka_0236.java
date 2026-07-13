public class kafka_0236 {

    private static final String OPTIMIZED_ASSIGNMENT_LOG_MESSAGE = "Detected that all consumers were subscribed to same set of topics, invoking the optimized assignment algorithm";
    private static final String GENERAL_ASSIGNMENT_LOG_MESSAGE = "Detected that not all consumers were subscribed to same set of topics, falling back to the general case assignment algorithm";

        @Override
        public Map<String, List<TopicPartition>> assignPartitions(Map<String, List<PartitionInfo>> partitionsPerTopic,
                                                                  Map<String, Subscription> subscriptions) {
            Map<String, List<TopicPartition>> consumerToOwnedPartitions = new HashMap<>();
            Set<TopicPartition> partitionsWithMultiplePreviousOwners = new HashSet<>();

            List<PartitionInfo> allPartitions = new ArrayList<>();
            partitionsPerTopic.values().forEach(allPartitions::addAll);
            RackInfo rackInfo = new RackInfo(allPartitions, subscriptions);

            AbstractAssignmentBuilder assignmentBuilder;
            if (allSubscriptionsEqual(partitionsPerTopic.keySet(), subscriptions, consumerToOwnedPartitions, partitionsWithMultiplePreviousOwners)) {
                log.debug(OPTIMIZED_ASSIGNMENT_LOG_MESSAGE);
                partitionsTransferringOwnership = new HashMap<>();
                assignmentBuilder = new ConstrainedAssignmentBuilder(partitionsPerTopic, rackInfo, consumerToOwnedPartitions, partitionsWithMultiplePreviousOwners);
            } else {
                log.debug(GENERAL_ASSIGNMENT_LOG_MESSAGE);
                // we must set this to null for the general case so the cooperative assignor knows to compute it from scratch
                partitionsTransferringOwnership = null;
                assignmentBuilder = new GeneralAssignmentBuilder(partitionsPerTopic, rackInfo, consumerToOwnedPartitions, subscriptions);
            }
            return assignmentBuilder.build();
        }
}
