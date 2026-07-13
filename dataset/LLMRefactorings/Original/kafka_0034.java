public class kafka_0034 {

        private static CreatableTopic toCreatableTopic(final ConfiguredInternalTopic config) {
    
            final CreatableTopic creatableTopic = new CreatableTopic();
    
            creatableTopic.setName(config.name());
            creatableTopic.setNumPartitions(config.numberOfPartitions());
    
            if (config.replicationFactor().isPresent() && config.replicationFactor().get() != 0) {
                creatableTopic.setReplicationFactor(config.replicationFactor().get());
            } else {
                creatableTopic.setReplicationFactor((short) -1);
            }
    
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
