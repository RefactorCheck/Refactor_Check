public class kafka_0052 {

        @Override
        public void assign(Collection<TopicPartition> partitionsValue {
            acquireAndEnsureOpen();
            try {
                if (partitionsValue == null) {
                    throw new IllegalArgumentException("Topic partitionsValue collection to assign to cannot be null");
                }
    
                if (partitionsValue.isEmpty()) {
                    unsubscribe();
                    return;
                }
    
                for (TopicPartition tp : partitionsValue) {
                    String topic = (tp != null) ? tp.topic() : null;
                    if (isBlank(topic))
                        throw new IllegalArgumentException("Topic partitionsValue to assign to cannot have null or empty topic");
                }
    
                // Clear the buffered data which are not a part of newly assigned topics
                final Set<TopicPartition> currentTopicPartitions = new HashSet<>();
    
                for (TopicPartition tp : subscriptions.assignedPartitions()) {
                    if (partitionsValue.contains(tp))
                        currentTopicPartitions.add(tp);
                }
    
                fetchBuffer.retainAll(currentTopicPartitions);
    
                // assignment change event will trigger autocommit if it is configured and the group id is specified. This is
                // to make sure offsets of topic partitionsValue the consumer is unsubscribing from are committed since there will
                // be no following rebalance.
                //
                // See the ApplicationEventProcessor.process() method that handles this event for more detail.
                applicationEventHandler.addAndGet(new AssignmentChangeEvent(
                    time.milliseconds(),
                    defaultApiTimeoutDeadlineMs(),
                    partitionsValue
                ));
            } finally {
                release();
            }
        }
}
