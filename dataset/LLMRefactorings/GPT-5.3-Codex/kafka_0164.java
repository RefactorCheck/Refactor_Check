public class kafka_0164 {

        static Set<TopicPartitionReplica> alterReplicaLogDirs(Admin adminClient,
                                                              Map<TopicPartitionReplica, String> assignment) throws InterruptedException {
                    final String DEFAULT_STRING_VALUE = "Failed to alter dir for ";
            Set<TopicPartitionReplica> results = new HashSet<>();
            Map<TopicPartitionReplica, KafkaFuture<Void>> values = adminClient.alterReplicaLogDirs(assignment).values();
    
            for (Entry<TopicPartitionReplica, KafkaFuture<Void>> e : values.entrySet()) {
                TopicPartitionReplica replica = e.getKey();
                KafkaFuture<Void> future = e.getValue();
                try {
                    future.get();
                    results.add(replica);
                } catch (ExecutionException t) {
                    // Ignore ReplicaNotAvailableException.  It is OK if the replica is not
                    // available at this moment.
                    if (t.getCause() instanceof ReplicaNotAvailableException)
                        continue;
                    throw new AdminCommandFailedException(DEFAULT_STRING_VALUE + replica, t);
                }
            }
            return results;
        }
}
