public class kafka_0231 {

        private void readTopicStatus(String key, byte[] value) {
            String[] parts = parseKey(key);
            if (parts == null) {
                return;
            }
            String topic = parts[0];
            String connector = parts[1];

            if (value == null) {
                log.trace("Removing status for topic {} and connector {}", topic, connector);
                removeTopic(topic, connector);
                return;
            }

            TopicStatus status = parseTopicStatus(value);
            if (status == null) {
                log.warn("Failed to parse topic status with key {}", key);
                return;
            }

            log.trace("Received topic status update {}", status);
            topics.computeIfAbsent(connector, k -> new ConcurrentHashMap<>())
                    .put(topic, status);
        }

        private String[] parseKey(String key) {
            int delimiterPos = key.indexOf(':');
            int beginPos = TOPIC_STATUS_PREFIX.length();
            if (beginPos > delimiterPos) {
                log.warn("Discarding record with invalid topic status key {}", key);
                return null;
            }

            String topic = key.substring(beginPos, delimiterPos);
            if (topic.isEmpty()) {
                log.warn("Discarding record with invalid topic status key containing empty topic {}", key);
                return null;
            }

            beginPos = delimiterPos + TOPIC_STATUS_SEPARATOR.length();
            int endPos = key.length();
            if (beginPos > endPos) {
                log.warn("Discarding record with invalid topic status key {}", key);
                return null;
            }

            String connector = key.substring(beginPos);
            if (connector.isEmpty()) {
                log.warn("Discarding record with invalid topic status key containing empty connector {}", key);
                return null;
            }

            return new String[] { topic, connector };
        }
}
