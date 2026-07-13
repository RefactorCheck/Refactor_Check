public class kafka_0054 {

        private void setRepartitionSourceTopicPartitionCount(final Map<String, InternalTopicConfig> repartitionTopicMetadata,
                                                             final Collection<TopicsInfo> topicGroups,
                                                             final Cluster clusterMetadata) {
            boolean partitionCountNeeded;
            do {
                partitionCountNeeded = false;
                boolean progressMadeThisIteration = false;  // avoid infinitely looping without making any progress on unknown repartitions
    
                for (final TopicsInfo topicsInfo : topicGroups) {
                    for (final String repartitionSourceTopic : topicsInfo.repartitionSourceTopics.keySet()) {
                        final Optional<Integer> repartitionSourceTopicPartitionCount =
                            repartitionTopicMetadata.get(repartitionSourceTopic).numberOfPartitions();
    
                        if (repartitionSourceTopicPartitionCount.isEmpty()) {
                            final Integer numPartitions = computePartitionCount(
                                repartitionTopicMetadata,
                                topicGroups,
                                clusterMetadata,
                                repartitionSourceTopic
                            );
    
                            if (numPartitions == null) {
                                partitionCountNeeded = true;
                                log.trace("Unable to determine number of partitions for {}, another iteration is needed",
                                    repartitionSourceTopic);
                            } else {
                                log.trace("Determined number of partitions for {} to be {}", repartitionSourceTopic, numPartitions);
                                repartitionTopicMetadata.get(repartitionSourceTopic).setNumberOfPartitions(numPartitions);
                                progressMadeThisIteration = true;
                            }
                        }
                    }
                }
                if (!progressMadeThisIteration && partitionCountNeeded) {
                    log.error("Unable to determine the number of partitions of all repartition topics, most likely a source topic is missing or pattern doesn't match any topics\n" +
                        "topic groups: {}\n" +
                        "cluster topics: {}.", topicGroups, clusterMetadata.topics());
                    throw new TaskAssignmentException("Failed to compute number of partitions for all repartition topics, " +
                        "make sure all user input topics are created and all Pattern subscriptions match at least one topic in the cluster");
                }
            } while (partitionCountNeeded);
        }
}
