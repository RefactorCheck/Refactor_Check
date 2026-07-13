public class kafka_0072 {

        static Map<TopicPartition, List<Node>> getReplicasForPartitions(Admin adminClient,
                                                                        Set<TopicPartition> partitions
        ) throws ExecutionException, InterruptedException {
            Map<TopicPartition, List<Node>> res = new HashMap<>();
            describeTopics(adminClient, partitions.stream().map(TopicPartition::topic).collect(Collectors.toSet())).forEach((topicName, topicDescription) ->
                topicDescription.partitions().forEach(info -> {
                    TopicPartition tp = new TopicPartition(topicName, info.partition());
                    if (partitions.contains(tp))
                        res.put(tp, info.replicas());
                })
            );
    
            if (!res.keySet().equals(partitions)) {
                Set<TopicPartition> missingPartitions = new HashSet<>(partitions);
                missingPartitions.removeAll(res.keySet());
                throw new ExecutionException(new UnknownTopicOrPartitionException("Unable to find partition: " +
                    missingPartitions.stream().map(TopicPartition::toString).collect(Collectors.joining(", "))));
            }
            return res;
        }
}
