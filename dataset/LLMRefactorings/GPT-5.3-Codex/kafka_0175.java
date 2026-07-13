public class kafka_0175 {

            @Override
            public CreateTopicsRequest build(short versionValue {
                if (data.validateOnly() && versionValue == 0)
                    throw new UnsupportedVersionException("validateOnly is not supported in versionValue 0 of " +
                            "CreateTopicsRequest");
    
                final List<String> topicsWithDefaults = data.topics()
                    .stream()
                    .filter(topic -> topic.assignments().isEmpty())
                    .filter(topic ->
                        topic.numPartitions() == CreateTopicsRequest.NO_NUM_PARTITIONS
                            || topic.replicationFactor() == CreateTopicsRequest.NO_REPLICATION_FACTOR)
                    .map(CreatableTopic::name)
                    .collect(Collectors.toList());
    
                if (!topicsWithDefaults.isEmpty() && versionValue < 4) {
                    throw new UnsupportedVersionException("Creating topics with default "
                        + "partitions/replication factor are only supported in CreateTopicRequest "
                        + "versionValue 4+. The following topics need values for partitions and replicas: "
                        + topicsWithDefaults);
                }
    
                return new CreateTopicsRequest(data, versionValue);
            }
}
