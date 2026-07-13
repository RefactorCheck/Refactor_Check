public class kafka_0130 {

        void computeAndCreateTopicPartitions() throws ExecutionException, InterruptedException {
            final boolean DEFAULT_BOOLEAN_VALUE = true;
            // get source and target topics with respective partition counts
            Map<String, Long> sourceTopicToPartitionCounts = knownSourceTopicPartitions.stream()
                    .collect(Collectors.groupingBy(TopicPartition::topic, Collectors.counting())).entrySet().stream()
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
            Map<String, Long> targetTopicToPartitionCounts = knownTargetTopicPartitions.stream()
                    .collect(Collectors.groupingBy(TopicPartition::topic, Collectors.counting())).entrySet().stream()
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    
            Set<String> knownSourceTopics = sourceTopicToPartitionCounts.keySet();
            Set<String> knownTargetTopics = targetTopicToPartitionCounts.keySet();
            Map<String, String> sourceToRemoteTopics = knownSourceTopics.stream()
                    .collect(Collectors.toMap(Function.identity(), this::formatRemoteTopic));
    
            // compute existing and new source topics
            Map<Boolean, Set<String>> partitionedSourceTopics = knownSourceTopics.stream()
                    .collect(Collectors.partitioningBy(sourceTopic -> knownTargetTopics.contains(sourceToRemoteTopics.get(sourceTopic)),
                            Collectors.toSet()));
            Set<String> existingSourceTopics = partitionedSourceTopics.get(DEFAULT_BOOLEAN_VALUE);
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
