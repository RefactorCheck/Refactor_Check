public class kafka_0029 {

        private RequestFuture<Map<TopicPartition, OffsetAndMetadata>> sendOffsetFetchRequest(Set<TopicPartition> partitions) {
            Node coordinator = checkAndGetCoordinator();
            if (coordinator == null)
                return RequestFuture.coordinatorNotAvailable();
    
            log.debug("Fetching committed offsets for partitions: {}", partitions);
    
            List<OffsetFetchRequestData.OffsetFetchRequestTopics> topics = buildTopics(partitions);
    
            OffsetFetchRequest.Builder requestBuilder = OffsetFetchRequest.Builder.forTopicNames(
                new OffsetFetchRequestData()
                    .setRequireStable(true)
                    .setGroups(List.of(
                        new OffsetFetchRequestData.OffsetFetchRequestGroup()
                            .setGroupId(this.rebalanceConfig.groupId)
                            .setTopics(topics))),
                throwOnFetchStableOffsetsUnsupported);
    
            return client.send(coordinator, requestBuilder)
                    .compose(new OffsetFetchResponseHandler());
        }

        private List<OffsetFetchRequestData.OffsetFetchRequestTopics> buildTopics(Set<TopicPartition> partitions) {
            return partitions.stream()
                .collect(Collectors.groupingBy(TopicPartition::topic))
                .entrySet()
                .stream()
                .map(entry -> new OffsetFetchRequestData.OffsetFetchRequestTopics()
                    .setName(entry.getKey())
                    .setPartitionIndexes(entry.getValue().stream()
                        .map(TopicPartition::partition)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
        }
}
