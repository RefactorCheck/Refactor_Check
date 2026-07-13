public class kafka_0266 {

        @Override
        public Map<TopicPartition, Throwable> handleUnsupportedVersionException(
            int brokerId, UnsupportedVersionException exception, Set<TopicPartition> keys
        ) {
            log.warn("Broker {} does not support MAX_TIMESTAMP offset specs", brokerId);
            Map<TopicPartition, Throwable> maxTimestampPartitions = collectMaxTimestampPartitions(keys, exception);
            if (maxTimestampPartitions.isEmpty()) {
                return keys.stream().collect(Collectors.toMap(k -> k, k -> exception));
            } else {
                return maxTimestampPartitions;
            }
        }

        private Map<TopicPartition, Throwable> collectMaxTimestampPartitions(
            Set<TopicPartition> keys, UnsupportedVersionException exception
        ) {
            Map<TopicPartition, Throwable> maxTimestampPartitions = new HashMap<>();
            for (TopicPartition topicPartition : keys) {
                Long offsetTimestamp = offsetTimestampsByPartition.get(topicPartition);
                if (offsetTimestamp == ListOffsetsRequest.MAX_TIMESTAMP) {
                    maxTimestampPartitions.put(topicPartition, exception);
                }
            }
            return maxTimestampPartitions;
        }
}
