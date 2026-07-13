public class kafka_0236 {

        @Override
        public Map<String, List<TopicPartition>> assignPartitions(Map<String, List<PartitionInfo>> partitionsPerTopic,
                                                                  Map<String, Subscription> subscriptions) {
            Map<String, List<TopicPartition>> consumerToOwnedPartitions = new HashMap<>();
            Set<TopicPartition> partitionsWithMultiplePreviousOwners = new HashSet<>();

            List<PartitionInfo> allPartitions = new ArrayList<>();
            partitionsPerTopic.values().forEach(allPartitions::addAll);
            RackInfo rackInfo = new RackInfo(allPartitions, subscriptions);

            AbstractAssignmentBuilder assignmentBuilder;
            final boolean allConsumersEqual = allSubscriptionsEqual(
                partitionsPerTopic.keySet(),
                subscriptions,
                consumerToOwnedPartitions,
                partitionsWithMultiplePreviousOwners);
            if (allConsumersEqual) {
                log.debug("Detected that all consumers were subscribed to same set of topics, invoking the "
                              + "optimized assignment algorithm");
                partitionsTransferringOwnership = new HashMap<>();
                assignmentBuilder = new ConstrainedAssignmentBuilder(partitionsPerTopic, rackInfo, consumerToOwnedPartitions, partitionsWithMultiplePreviousOwners);
            } else {
                log.debug("Detected that not all consumers were subscribed to same set of topics, falling back to the "
                              + "general case assignment algorithm");
                partitionsTransferringOwnership = null;
                assignmentBuilder = new GeneralAssignmentBuilder(partitionsPerTopic, rackInfo, consumerToOwnedPartitions, subscriptions);
            }
            return assignmentBuilder.build();
        }
}
