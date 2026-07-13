public class kafka_0256 {

    public static AssignReplicasToDirsResponseData buildResponseData(short errorCode, int throttleTimeMs, Map<Uuid, Map<TopicIdPartition, Errors>> errors) {
        Map<Uuid, AssignReplicasToDirsResponseData.DirectoryData> directoryMap = new HashMap<>();
        Map<Uuid, Map<Uuid, AssignReplicasToDirsResponseData.TopicData>> topicMap = new HashMap<>();
        for (Map.Entry<Uuid, Map<TopicIdPartition, Errors>> dirEntry : errors.entrySet()) {
            Uuid directoryId = dirEntry.getKey();
            AssignReplicasToDirsResponseData.DirectoryData directory = directoryMap.computeIfAbsent(directoryId, d -> new AssignReplicasToDirsResponseData.DirectoryData().setId(directoryId));
            for (Map.Entry<TopicIdPartition, Errors> partitionEntry : dirEntry.getValue().entrySet()) {
                processPartition(directory, directoryId, partitionEntry, topicMap);
            }
        }
        return new AssignReplicasToDirsResponseData()
                .setErrorCode(errorCode)
                .setThrottleTimeMs(throttleTimeMs)
                .setDirectories(new ArrayList<>(directoryMap.values()));
    }

    private static void processPartition(AssignReplicasToDirsResponseData.DirectoryData directory, Uuid directoryId,
                                          Map.Entry<TopicIdPartition, Errors> partitionEntry,
                                          Map<Uuid, Map<Uuid, AssignReplicasToDirsResponseData.TopicData>> topicMap) {
        TopicIdPartition topicPartition = partitionEntry.getKey();
        Errors error = partitionEntry.getValue();
        AssignReplicasToDirsResponseData.TopicData topic = topicMap.computeIfAbsent(directoryId, d -> new HashMap<>())
                .computeIfAbsent(topicPartition.topicId(), topicId -> {
                    AssignReplicasToDirsResponseData.TopicData data = new AssignReplicasToDirsResponseData.TopicData().setTopicId(topicId);
                    directory.topics().add(data);
                    return data;
                });
        AssignReplicasToDirsResponseData.PartitionData partition = new AssignReplicasToDirsResponseData.PartitionData()
                .setPartitionIndex(topicPartition.partitionId()).setErrorCode(error.code());
        topic.partitions().add(partition);
    }
}
