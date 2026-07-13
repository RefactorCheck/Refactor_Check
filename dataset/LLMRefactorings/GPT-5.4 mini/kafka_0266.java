public class kafka_0266 {

        private static final String MAX_TIMESTAMP_UNSUPPORTED_MESSAGE =
            "Broker {} does not support MAX_TIMESTAMP offset specs";

        @Override
        public Map<TopicPartition, Throwable> handleUnsupportedVersionException(
            int brokerId, UnsupportedVersionException exception, Set<TopicPartition> keys
        ) {
            log.warn(MAX_TIMESTAMP_UNSUPPORTED_MESSAGE, brokerId);
            Map<TopicPartition, Throwable> maxTimestampPartitions = new HashMap<>();
            for (TopicPartition topicPartition : keys) {
                Long offsetTimestamp = offsetTimestampsByPartition.get(topicPartition);
                if (offsetTimestamp == ListOffsetsRequest.MAX_TIMESTAMP) {
                    maxTimestampPartitions.put(topicPartition, exception);
                }
            }
            // If there are no partitions with MAX_TIMESTAMP specs the UnsupportedVersionException cannot be handled
            // and all partitions should be failed here.
            // Otherwise, just the partitions with MAX_TIMESTAMP specs should be failed here and the fulfillment stage
            // will later be retried for the potentially empty set of partitions with non-MAX_TIMESTAMP specs.
            if (maxTimestampPartitions.isEmpty()) {
                return keys.stream().collect(Collectors.toMap(k -> k, k -> exception));
            } else {
                return maxTimestampPartitions;
            }
        }
}
