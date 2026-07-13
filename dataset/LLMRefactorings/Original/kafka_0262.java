public class kafka_0262 {

        @Override
        public void onTasksRevoked(final Set<StreamsRebalanceData.TaskId> tasks) {
            final Map<TaskId, Set<TopicPartition>> activeTasksToRevokeWithPartitions =
                pairWithTopicPartitions(tasks.stream());
            final Set<TopicPartition> partitionsToRevoke = activeTasksToRevokeWithPartitions.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    
            final long start = time.milliseconds();
            try {
                log.info("Revoking active tasks {}.", tasks);
                taskManager.handleRevocation(partitionsToRevoke);
            } finally {
                final long latency = time.milliseconds() - start;
                tasksRevokedSensor.record(latency);
                log.info("partition revocation took {} ms.", latency);
            }
            if (streamThread.state() != StreamThread.State.PENDING_SHUTDOWN) {
                streamThread.setState(StreamThread.State.PARTITIONS_REVOKED);
            }
        }
}
