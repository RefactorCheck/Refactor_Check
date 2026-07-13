public class kafka_0181 {

            private static AddPartitionsToTxnTopicCollection buildTxnTopicCollection(final List<TopicPartition> partitions) {
                Map<String, List<Integer>> partitionMap = new HashMap<>();
                for (TopicPartition topicPartition : partitions) {
                    String topicName = topicPartition.topic();
    
                    partitionMap.compute(topicName, (key, subPartitions) -> {
                        if (subPartitions == null) {
                            subPartitions = new ArrayList<>();
                        }
                        subPartitions.add(topicPartition.partition());
                        return subPartitions;
                    });
                }
    
                AddPartitionsToTxnTopicCollection topics = new AddPartitionsToTxnTopicCollection();
                for (Map.Entry<String, List<Integer>> partitionEntry : partitionMap.entrySet()) {
                    topics.add(new AddPartitionsToTxnTopic()
                        .setName(partitionEntry.getKey())
                        .setPartitions(partitionEntry.getValue()));
                }
                return topics;
            }
}
