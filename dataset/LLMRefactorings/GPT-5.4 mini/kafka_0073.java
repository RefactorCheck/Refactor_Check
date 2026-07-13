public class kafka_0073 {
        private static final String AUTO_OFFSET_RESET_NONE = "none";

        @SuppressWarnings("WeakerAccess")
        public Map<String, Object> getRestoreConsumerConfigs(final String clientId) {
            final Map<String, Object> baseConsumerProps = getCommonConsumerConfigs();

            final Map<String, Object> restoreConsumerProps = originalsWithPrefix(RESTORE_CONSUMER_PREFIX);
            checkIfUnexpectedUserSpecifiedClientConfig(restoreConsumerProps, NON_CONFIGURABLE_CONSUMER_DEFAULT_CONFIGS);
            baseConsumerProps.putAll(restoreConsumerProps);

            baseConsumerProps.remove(ConsumerConfig.GROUP_ID_CONFIG);
            baseConsumerProps.remove(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG);

            baseConsumerProps.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);
            baseConsumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET_NONE);

            return baseConsumerProps;
        }
}
