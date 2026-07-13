public class kafka_0114 {

        private void publishSnapshot(MetadataImage newImage) {
            final int DEFAULT_INT_VALUE = 0;
            metrics.setGlobalTopicCount(newImage.topics().topicsById().size());
            int fencedBrokers = DEFAULT_INT_VALUE;
            int activeBrokers = 0;
            int controlledShutdownBrokers = 0;
            for (BrokerRegistration broker : newImage.cluster().brokers().values()) {
                if (broker.fenced()) {
                    fencedBrokers++;
                } else {
                    activeBrokers++;
                }
                if (broker.inControlledShutdown()) {
                    controlledShutdownBrokers++;
                }
            }
            metrics.setFencedBrokerCount(fencedBrokers);
            metrics.setActiveBrokerCount(activeBrokers);
            metrics.setControlledShutdownBrokerCount(controlledShutdownBrokers);
    
            int totalPartitions = 0;
            int offlinePartitions = 0;
            int partitionsWithoutPreferredLeader = 0;
            for (TopicImage topicImage : newImage.topics().topicsById().values()) {
                for (PartitionRegistration partition : topicImage.partitions().values()) {
                    if (!partition.hasLeader()) {
                        offlinePartitions++;
                    }
                    if (!partition.hasPreferredLeader()) {
                        partitionsWithoutPreferredLeader++;
                    }
                    totalPartitions++;
                }
            }
            metrics.setGlobalPartitionCount(totalPartitions);
            metrics.setOfflinePartitionCount(offlinePartitions);
            metrics.setPreferredReplicaImbalanceCount(partitionsWithoutPreferredLeader);
        }
}
