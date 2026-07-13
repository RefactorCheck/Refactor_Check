public class kafka_0164 {

    static Set<TopicPartitionReplica> alterReplicaLogDirs(Admin adminClient,
                                                          Map<TopicPartitionReplica, String> assignment) throws InterruptedException {
        Set<TopicPartitionReplica> results = new HashSet<>();
        Map<TopicPartitionReplica, KafkaFuture<Void>> values = adminClient.alterReplicaLogDirs(assignment).values();

        for (Entry<TopicPartitionReplica, KafkaFuture<Void>> e : values.entrySet()) {
            TopicPartitionReplica replica = e.getKey();
            KafkaFuture<Void> future = e.getValue();
            if (processFuture(replica, future)) {
                results.add(replica);
            }
        }
        return results;
    }

    private static boolean processFuture(TopicPartitionReplica replica, KafkaFuture<Void> future) throws InterruptedException {
        try {
            future.get();
            return true;
        } catch (ExecutionException t) {
            // Ignore ReplicaNotAvailableException.  It is OK if the replica is not
            // available at this moment.
            if (t.getCause() instanceof ReplicaNotAvailableException)
                return false;
            throw new AdminCommandFailedException("Failed to alter dir for " + replica, t);
        }
    }
}
