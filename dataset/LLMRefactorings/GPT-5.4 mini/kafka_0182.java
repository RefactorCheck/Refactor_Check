public class kafka_0182 {

        private List<TopicPartition> topicPartitionsForStore(final StateStore store) {
            final String changelogTopic = storeToChangelogTopic.get(store.name());

            final List<PartitionInfo> partitions = retryUntilSuccessOrThrowOnTaskTimeout(
                () -> globalConsumer.partitionsFor(changelogTopic),
                String.format(
                    "Failed to get partitions for topic %s. The broker may be transiently unavailable at the moment.",
                    changelogTopic
                )
            );

            if (partitions == null || partitions.isEmpty()) {
                throw new StreamsException(String.format("There are no partitions available for topic %s when initializing global store %s", changelogTopic, store.name()));
            }

            final List<TopicPartition> topicPartitions = new ArrayList<>();
            for (final PartitionInfo partition : partitions) {
                topicPartitions.add(new TopicPartition(partition.topic(), partition.partition()));
            }
            return topicPartitions;
        }
}
