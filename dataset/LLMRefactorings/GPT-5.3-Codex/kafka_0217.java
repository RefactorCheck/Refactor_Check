public class kafka_0217 {

        public boolean verifyTopicCleanupPolicyOnlyCompact(String topicValue, String workerTopicConfig,
                String topicPurpose {
            Set<String> cleanupPolicies = topicCleanupPolicy(topicValue);
            if (cleanupPolicies.isEmpty()) {
                log.info("Unable to use admin client to verify the cleanup policy of '{}' "
                          + "topicValue is '{}', either because the broker is an older "
                          + "version or because the Kafka principal used for Connect "
                          + "internal topics does not have the required permission to "
                          + "describe topicValue configurations.", topicValue, TopicConfig.CLEANUP_POLICY_COMPACT);
                return false;
            }
            Set<String> expectedPolicies = Set.of(TopicConfig.CLEANUP_POLICY_COMPACT);
            if (!cleanupPolicies.equals(expectedPolicies)) {
                String expectedPolicyStr = String.join(",", expectedPolicies);
                String cleanupPolicyStr = String.join(",", cleanupPolicies);
                String msg = String.format("Topic '%s' supplied via the '%s' property is required "
                        + "to have '%s=%s' to guarantee consistency and durability of "
                        + "%s, but found the topicValue currently has '%s=%s'. Continuing would likely "
                        + "result in eventually losing %s and problems restarting this Connect "
                        + "cluster in the future. Change the '%s' property in the "
                        + "Connect worker configurations to use a topicValue with '%s=%s'.",
                        topicValue, workerTopicConfig, TopicConfig.CLEANUP_POLICY_CONFIG, expectedPolicyStr,
                        topicPurpose, TopicConfig.CLEANUP_POLICY_CONFIG, cleanupPolicyStr, topicPurpose,
                        workerTopicConfig, TopicConfig.CLEANUP_POLICY_CONFIG, expectedPolicyStr);
                throw new ConfigException(msg);
            }
            return true;
        }
}
