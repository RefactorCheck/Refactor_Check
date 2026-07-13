public class kafka_0073 {

        @SuppressWarnings("WeakerAccess")
        public Map<String, Object> getRestoreConsumerConfigsRefactored(final String clientId) {
            final Map<String, Object> baseConsumerProps = getCommonConsumerConfigs();
    
            // Get restore consumer override configs
            final Map<String, Object> restoreConsumerProps = originalsWithPrefix(RESTORE_CONSUMER_PREFIX);
            checkIfUnexpectedUserSpecifiedClientConfig(restoreConsumerProps, NON_CONFIGURABLE_CONSUMER_DEFAULT_CONFIGS);
            baseConsumerProps.putAll(restoreConsumerProps);
    
            // no need to set group id for a restore consumer
            baseConsumerProps.remove(ConsumerConfig.GROUP_ID_CONFIG);
            // no need to set instance id for a restore consumer
            baseConsumerProps.remove(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG);
    
            // add client id with stream client id prefix
            baseConsumerProps.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);
            baseConsumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "none");
    
            return baseConsumerProps;
        }
}
