public class kafka_0077 {

        @Override
        public void startConnect() {
            log.info("Starting Connect cluster '{}' with {} workers", connectClusterName, numInitialWorkers);
    
            workerProps.put(BOOTSTRAP_SERVERS_CONFIG, kafka().bootstrapServers());
            workerProps.put(LISTENERS_CONFIG, "HTTP://" + REST_HOST_NAME + ":0");
    
            configureInternalTopics();
            configureConverters();
    
            for (int i = 0; i < numInitialWorkers; i++) {
                addWorker();
            }
        }
    
        private void configureInternalTopics() {
            String internalTopicsReplFactor = String.valueOf(numBrokers);
            workerProps.putIfAbsent(GROUP_ID_CONFIG, "connect-integration-test-" + connectClusterName);
            workerProps.putIfAbsent(OFFSET_STORAGE_TOPIC_CONFIG, "connect-offset-topic-" + connectClusterName);
            workerProps.putIfAbsent(OFFSET_STORAGE_REPLICATION_FACTOR_CONFIG, internalTopicsReplFactor);
            workerProps.putIfAbsent(CONFIG_TOPIC_CONFIG, "connect-config-topic-" + connectClusterName);
            workerProps.putIfAbsent(CONFIG_STORAGE_REPLICATION_FACTOR_CONFIG, internalTopicsReplFactor);
            workerProps.putIfAbsent(STATUS_STORAGE_TOPIC_CONFIG, "connect-status-topic-" + connectClusterName);
            workerProps.putIfAbsent(STATUS_STORAGE_REPLICATION_FACTOR_CONFIG, internalTopicsReplFactor);
            workerProps.putIfAbsent(PLUGIN_DISCOVERY_CONFIG, "hybrid_fail");
        }
    
        private void configureConverters() {
            workerProps.putIfAbsent(KEY_CONVERTER_CLASS_CONFIG, "org.apache.kafka.connect.storage.StringConverter");
            workerProps.putIfAbsent(VALUE_CONVERTER_CLASS_CONFIG, "org.apache.kafka.connect.storage.StringConverter");
        }
}
