public class kafka_0217 {

        public boolean verifyTopicCleanupPolicyOnlyCompact(String topic, String workerTopicConfig,
                String topicPurpose) {
            Set<String> cleanupPolicies = topicCleanupPolicy(topic);
            if (cleanupPolicies.isEmpty()) {
                log.info("Unable to use admin client to verify the cleanup policy of '{}' "
                          + "topic is '{}', either because the broker is an older "
                          + "version or because the Kafka principal used for Connect "
                          + "internal topics does not have the required permission to "
                          + "describe topic configurations.", topic, TopicConfig.CLEANUP_POLICY_COMPACT);
                return false;
            }
            Set<String> expectedPolicies = Set.of(TopicConfig.CLEANUP_POLICY_COMPACT);
            if (!cleanupPolicies.equals(expectedPolicies)) {
                throw new ConfigException(cleanupPolicyMismatchMessage(topic, workerTopicConfig, topicPurpose,
                    expectedPolicies, cleanupPolicies));
            }
            return true;
        }

        private String cleanupPolicyMismatchMessage(String topic, String workerTopicConfig, String topicPurpose,
                Set<String> expectedPolicies, Set<String> cleanupPolicies) {
            String expectedPolicyStr = String.join(",", expectedPolicies);
            String cleanupPolicyStr = String.join(",", cleanupPolicies);
            return String.format("Topic '%s' supplied via the '%s' property is required "
                    + "to have '%s=%s' to guarantee consistency and durability of "
                    + "%s, but found the topic currently has '%s=%s'. Continuing would likely "
                    + "result in eventually losing %s and problems restarting this Connect "
                    + "cluster in the future. Change the '%s' property in the "
                    + "Connect worker configurations to use a topic with '%s=%s'.",
                    topic, workerTopicConfig, TopicConfig.CLEANUP_POLICY_CONFIG, expectedPolicyStr,
                    topicPurpose, TopicConfig.CLEANUP_POLICY_CONFIG, cleanupPolicyStr, topicPurpose,
                    workerTopicConfig, TopicConfig.CLEANUP_POLICY_CONFIG, expectedPolicyStr);
        }
}
