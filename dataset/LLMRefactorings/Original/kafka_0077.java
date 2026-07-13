public class kafka_0077 {

        @Override
        public void startConnect() {
            log.info("Starting Connect cluster '{}' with {} workers", connectClusterName, numInitialWorkers);
    
            workerProps.put(BOOTSTRAP_SERVERS_CONFIG, kafka().bootstrapServers());
            // use a random available port
            workerProps.put(LISTENERS_CONFIG, "HTTP://" + REST_HOST_NAME + ":0");
    
            String internalTopicsReplFactor = String.valueOf(numBrokers);
            workerProps.putIfAbsent(GROUP_ID_CONFIG, "connect-integration-test-" + connectClusterName);
            workerProps.putIfAbsent(OFFSET_STORAGE_TOPIC_CONFIG, "connect-offset-topic-" + connectClusterName);
            workerProps.putIfAbsent(OFFSET_STORAGE_REPLICATION_FACTOR_CONFIG, internalTopicsReplFactor);
            workerProps.putIfAbsent(CONFIG_TOPIC_CONFIG, "connect-config-topic-" + connectClusterName);
            workerProps.putIfAbsent(CONFIG_STORAGE_REPLICATION_FACTOR_CONFIG, internalTopicsReplFactor);
            workerProps.putIfAbsent(STATUS_STORAGE_TOPIC_CONFIG, "connect-status-topic-" + connectClusterName);
            workerProps.putIfAbsent(STATUS_STORAGE_REPLICATION_FACTOR_CONFIG, internalTopicsReplFactor);
            workerProps.putIfAbsent(KEY_CONVERTER_CLASS_CONFIG, "org.apache.kafka.connect.storage.StringConverter");
            workerProps.putIfAbsent(VALUE_CONVERTER_CLASS_CONFIG, "org.apache.kafka.connect.storage.StringConverter");
            workerProps.putIfAbsent(PLUGIN_DISCOVERY_CONFIG, "hybrid_fail");
    
            for (int i = 0; i < numInitialWorkers; i++) {
                addWorker();
            }
        }
}
