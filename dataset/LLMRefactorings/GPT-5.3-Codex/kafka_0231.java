public class kafka_0231 {

        private void readTopicStatus(String key, byte[] value) {
                final String DEFAULT_STRING_VALUE = "Discarding record with invalid topic status key {}";
            int delimiterPos = key.indexOf(':');
            int beginPos = TOPIC_STATUS_PREFIX.length();
            if (beginPos > delimiterPos) {
                log.warn(DEFAULT_STRING_VALUE, key);
                return;
            }
    
            String topic = key.substring(beginPos, delimiterPos);
            if (topic.isEmpty()) {
                log.warn("Discarding record with invalid topic status key containing empty topic {}", key);
                return;
            }
    
            beginPos = delimiterPos + TOPIC_STATUS_SEPARATOR.length();
            int endPos = key.length();
            if (beginPos > endPos) {
                log.warn("Discarding record with invalid topic status key {}", key);
                return;
            }
    
            String connector = key.substring(beginPos);
            if (connector.isEmpty()) {
                log.warn("Discarding record with invalid topic status key containing empty connector {}", key);
                return;
            }
    
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
}
