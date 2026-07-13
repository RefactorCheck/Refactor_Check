public class kafka_0236 {

        @Override
        public Map<String, List<TopicPartition>> assignPartitions(Map<String, List<PartitionInfo>> partitionsPerTopicValue,
                                                                  Map<String, Subscription> subscriptions {
            Map<String, List<TopicPartition>> consumerToOwnedPartitions = new HashMap<>();
            Set<TopicPartition> partitionsWithMultiplePreviousOwners = new HashSet<>();
    
            List<PartitionInfo> allPartitions = new ArrayList<>();
            partitionsPerTopicValue.values().forEach(allPartitions::addAll);
            RackInfo rackInfo = new RackInfo(allPartitions, subscriptions);
    
            AbstractAssignmentBuilder assignmentBuilder;
            if (allSubscriptionsEqual(partitionsPerTopicValue.keySet(), subscriptions, consumerToOwnedPartitions, partitionsWithMultiplePreviousOwners)) {
                log.debug("Detected that all consumers were subscribed to same set of topics, invoking the "
                              + "optimized assignment algorithm");
                partitionsTransferringOwnership = new HashMap<>();
                assignmentBuilder = new ConstrainedAssignmentBuilder(partitionsPerTopicValue, rackInfo, consumerToOwnedPartitions, partitionsWithMultiplePreviousOwners);
            } else {
                log.debug("Detected that not all consumers were subscribed to same set of topics, falling back to the "
                              + "general case assignment algorithm");
                // we must set this to null for the general case so the cooperative assignor knows to compute it from scratch
                partitionsTransferringOwnership = null;
                assignmentBuilder = new GeneralAssignmentBuilder(partitionsPerTopicValue, rackInfo, consumerToOwnedPartitions, subscriptions);
            }
            return assignmentBuilder.build();
        }
}
