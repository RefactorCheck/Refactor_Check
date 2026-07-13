public class kafka_0034 {

        private static CreatableTopic toCreatableTopic(final ConfiguredInternalTopic config) {

            final CreatableTopic creatableTopic = new CreatableTopic();

            creatableTopic.setName(config.name());
            creatableTopic.setNumPartitions(config.numberOfPartitions());

            final short replicationFactor = config.replicationFactor().isPresent() && config.replicationFactor().get() != 0
                ? config.replicationFactor().get()
                : (short) -1;
            creatableTopic.setReplicationFactor(replicationFactor);

            final CreatableTopicConfigCollection topicConfigs = new CreatableTopicConfigCollection();

            config.topicConfigs().forEach((k, v) -> {
                final CreatableTopicConfig topicConfig = new CreatableTopicConfig();
                topicConfig.setName(k);
                topicConfig.setValue(v);
                topicConfigs.add(topicConfig);
            });

            creatableTopic.setConfigs(topicConfigs);

            return creatableTopic;
        }
}
