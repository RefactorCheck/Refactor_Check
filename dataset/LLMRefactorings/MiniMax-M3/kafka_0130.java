public class kafka_0130 {

    private Map<String, Long> countPartitionsByTopic(Set<TopicPartition> topicPartitions) {
        return topicPartitions.stream()
                .collect(Collectors.groupingBy(TopicPartition::topic, Collectors.counting())).entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    void computeAndCreateTopicPartitions() throws ExecutionException, InterruptedException {
        // get source and target topics with respective partition counts
        Map<String, Long> sourceTopicToPartitionCounts = countPartitionsByTopic(knownSourceTopicPartitions);
        Map<String, Long> targetTopicToPartitionCounts = countPartitionsByTopic(knownTargetTopicPartitions);

        Set<String> knownSourceTopics = sourceTopicToPartitionCounts.keySet();
        Set<String> knownTargetTopics = targetTopicToPartitionCounts.keySet();
        Map<String, String> sourceToRemoteTopics = knownSourceTopics.stream()
                .collect(Collectors.toMap(Function.identity(), this::formatRemoteTopic));

        // compute existing and new source topics
        Map<Boolean, Set<String>> partitionedSourceTopics = knownSourceTopics.stream()
                .collect(Collectors.partitioningBy(sourceTopic -> knownTargetTopics.contains(sourceToRemoteTopics.get(sourceTopic)),
                        Collectors.toSet()));
        Set<String> existingSourceTopics = partitionedSourceTopics.get(true);
        Set<String> newSourceTopics = partitionedSourceTopics.get(false);

        // create new topics
        if (!newSourceTopics.isEmpty())
            createNewTopics(newSourceTopics, sourceTopicToPartitionCounts);

        // compute topics with new partitions
        Map<String, Long> sourceTopicsWithNewPartitions = existingSourceTopics.stream()
                .filter(sourceTopic -> {
                    String targetTopic = sourceToRemoteTopics.get(sourceTopic);
                    return sourceTopicToPartitionCounts.get(sourceTopic) > targetTopicToPartitionCounts.get(targetTopic);
                })
                .collect(Collectors.toMap(Function.identity(), sourceTopicToPartitionCounts::get));

        // create new partitions
        if (!sourceTopicsWithNewPartitions.isEmpty()) {
            Map<String, NewPartitions> newTargetPartitions = sourceTopicsWithNewPartitions.entrySet().stream()
                    .collect(Collectors.toMap(sourceTopicAndPartitionCount -> sourceToRemoteTopics.get(sourceTopicAndPartitionCount.getKey()),
                        sourceTopicAndPartitionCount -> NewPartitions.increaseTo(sourceTopicAndPartitionCount.getValue().intValue())));
            createNewPartitions(newTargetPartitions);
        }
    }
}
