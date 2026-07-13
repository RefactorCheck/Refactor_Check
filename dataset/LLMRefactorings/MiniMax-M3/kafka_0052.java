public class kafka_0052 {

    private static final String NULL_TOPIC_PARTITIONS_MESSAGE = "Topic partitions collection to assign to cannot be null";
    private static final String INVALID_TOPIC_MESSAGE = "Topic partitions to assign to cannot have null or empty topic";

    @Override
    public void assign(Collection<TopicPartition> partitions) {
        acquireAndEnsureOpen();
        try {
            if (partitions == null) {
                throw new IllegalArgumentException(NULL_TOPIC_PARTITIONS_MESSAGE);
            }

            if (partitions.isEmpty()) {
                unsubscribe();
                return;
            }

            validateTopicPartitions(partitions);

            final Set<TopicPartition> currentTopicPartitions = new HashSet<>();
            for (TopicPartition tp : subscriptions.assignedPartitions()) {
                if (partitions.contains(tp))
                    currentTopicPartitions.add(tp);
            }

            fetchBuffer.retainAll(currentTopicPartitions);

            applicationEventHandler.addAndGet(new AssignmentChangeEvent(
                time.milliseconds(),
                defaultApiTimeoutDeadlineMs(),
                partitions
            ));
        } finally {
            release();
        }
    }

    private void validateTopicPartitions(Collection<TopicPartition> partitions) {
        for (TopicPartition tp : partitions) {
            String topic = (tp != null) ? tp.topic() : null;
            if (isBlank(topic))
                throw new IllegalArgumentException(INVALID_TOPIC_MESSAGE);
        }
    }
}
