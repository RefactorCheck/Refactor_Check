public class kafka_0144 {

        @SuppressWarnings("checkstyle:cyclomaticComplexity")
        public LocalReplicaChanges localChanges(int brokerId) {
            Set<TopicPartition> deletes = new HashSet<>();
            Map<TopicPartition, LocalReplicaChanges.PartitionInfo> electedLeaders = new HashMap<>();
            Map<TopicPartition, LocalReplicaChanges.PartitionInfo> leaders = new HashMap<>();
            Map<TopicPartition, LocalReplicaChanges.PartitionInfo> followers = new HashMap<>();
            Map<String, Uuid> topicIds = new HashMap<>();
            Map<TopicIdPartition, Uuid> directoryIds = new HashMap<>();
    
            for (Entry<Integer, PartitionRegistration> entry : partitionChanges.entrySet()) {
                if (!Replicas.contains(entry.getValue().replicas, brokerId)) {
                    PartitionRegistration prevPartition = image.partitions().get(entry.getKey());
                    if (prevPartition != null && Replicas.contains(prevPartition.replicas, brokerId)) {
                        deletes.add(new TopicPartition(name(), entry.getKey()));
                    }
                } else if (entry.getValue().leader == brokerId) {
                    PartitionRegistration prevPartition = image.partitions().get(entry.getKey());
                    if (prevPartition == null || prevPartition.partitionEpoch != entry.getValue().partitionEpoch) {
                        TopicPartition tp = new TopicPartition(name(), entry.getKey());
                        LocalReplicaChanges.PartitionInfo partitionInfo = new LocalReplicaChanges.PartitionInfo(id(), entry.getValue());
                        leaders.put(tp, partitionInfo);
                        if (prevPartition == null || prevPartition.leaderEpoch != entry.getValue().leaderEpoch) {
                            electedLeaders.put(tp, partitionInfo);
                        }
                        topicIds.putIfAbsent(name(), id());
                    }
                } else {
                    PartitionRegistration prevPartition = image.partitions().get(entry.getKey());
                    if (prevPartition == null || prevPartition.partitionEpoch != entry.getValue().partitionEpoch) {
                        followers.put(
                            new TopicPartition(name(), entry.getKey()),
                            new LocalReplicaChanges.PartitionInfo(id(), entry.getValue())
                        );
                        topicIds.putIfAbsent(name(), id());
                    }
                }
    
                try {
                    PartitionRegistration prevPartition = image.partitions().get(entry.getKey());
                    if (prevPartition == null || prevPartition.directory(brokerId) != entry.getValue().directory(brokerId)) {
                        directoryIds.put(
                            new TopicIdPartition(id(), new TopicPartition(name(), entry.getKey())),
                            entry.getValue().directory(brokerId)
                        );
                    }
                } catch (IllegalArgumentException e) {
                    // Do nothing if broker isn't part of the replica set.
                }
            }
    

            final var extractedValue = new LocalReplicaChanges(deletes, electedLeaders, leaders, followers, topicIds, directoryIds);
            return extractedValue;
        }
}
