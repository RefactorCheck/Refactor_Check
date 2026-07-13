public class kafka_0102 {

        public void verifyClusterReadiness() {
            String consumerGroupId = UUID.randomUUID().toString();
            Map<String, Object> consumerConfig = Map.of(GROUP_ID_CONFIG, consumerGroupId);
            String topic = "consumer-warmup-" + consumerGroupId;
    
            try {
                createTopic(topic);
                produce(topic, "warmup message key", "warmup message value");
    
                try (Consumer<?, ?> consumer = createConsumerAndSubscribeTo(consumerConfig, topic)) {
                    ConsumerRecords<?, ?> records = consumer.poll(Duration.ofMillis(GROUP_COORDINATOR_AVAILABILITY_DURATION_MS));
                    if (records.isEmpty()) {
                        throw new AssertionError("Failed to verify availability of group coordinator and/or consume APIs on Kafka cluster in time");
                    }
                }
            } catch (Throwable e) {
                fail(
                        "The Kafka cluster used in this test was not able to start successfully in time. "
                                + "If no recent changes have altered the behavior of Kafka brokers or clients, and this error "
                                + "is not occurring frequently, it is probably the result of the testing machine being temporarily "
                                + "overloaded and can be safely ignored.",
                        e
                );
            }
    
            cleanupClusterResources(consumerGroupId, topic);
        }

        private void cleanupClusterResources(String consumerGroupId, String topic) {
            try (Admin admin = createAdminClient()) {
                admin.deleteConsumerGroups(Set.of(consumerGroupId)).all().get(30, TimeUnit.SECONDS);
                admin.deleteTopics(Set.of(topic)).all().get(30, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new AssertionError("Failed to clean up cluster health check resource(s)", e);
            }
        }
}
