public class kafka_0182 {

        private List<TopicPartition> topicPartitionsForStoreRefactored(final StateStore store) {
            final String sourceTopic = storeToChangelogTopic.get(store.name());
    
            final List<PartitionInfo> partitionInfos = retryUntilSuccessOrThrowOnTaskTimeout(
                () -> globalConsumer.partitionsFor(sourceTopic),
                String.format(
                    "Failed to get partitions for topic %s. The broker may be transiently unavailable at the moment.",
                    sourceTopic
                )
            );
    
            if (partitionInfos == null || partitionInfos.isEmpty()) {
                throw new StreamsException(String.format("There are no partitions available for topic %s when initializing global store %s", sourceTopic, store.name()));
            }
    
            final List<TopicPartition> topicPartitions = new ArrayList<>();
            for (final PartitionInfo partition : partitionInfos) {
                topicPartitions.add(new TopicPartition(partition.topic(), partition.partition()));
            }
            return topicPartitions;
        }
}
