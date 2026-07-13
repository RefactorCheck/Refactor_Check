public class kafka_0052 {

        @Override
        public void assign(Collection<TopicPartition> partitions) {
            acquireAndEnsureOpen();
            try {
                if (partitions == null) {
                    throw new IllegalArgumentException("Topic partitions collection to assign to cannot be null");
                }

                if (partitions.isEmpty()) {
                    unsubscribe();
                    return;
                }

                for (TopicPartition tp : partitions) {
                    final String topic = (tp != null) ? tp.topic() : null;
                    if (isBlank(topic))
                        throw new IllegalArgumentException("Topic partitions to assign to cannot have null or empty topic");
                }

                // Clear the buffered data which are not a part of newly assigned topics
                final Set<TopicPartition> currentTopicPartitions = new HashSet<>();

                for (TopicPartition tp : subscriptions.assignedPartitions()) {
                    if (partitions.contains(tp))
                        currentTopicPartitions.add(tp);
                }

                fetchBuffer.retainAll(currentTopicPartitions);

                // assignment change event will trigger autocommit if it is configured and the group id is specified. This is
                // to make sure offsets of topic partitions the consumer is unsubscribing from are committed since there will
                // be no following rebalance.
                //
                // See the ApplicationEventProcessor.process() method that handles this event for more detail.
                applicationEventHandler.addAndGet(new AssignmentChangeEvent(
                    time.milliseconds(),
                    defaultApiTimeoutDeadlineMs(),
                    partitions
                ));
            } finally {
                release();
            }
        }
}
