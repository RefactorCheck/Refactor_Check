public class kafka_0003 {

        private AlterPartitionRequest.Builder buildRequest(
                List<AlterPartitionItem> inflightAlterPartitionItems,
                long brokerEpoch) {
            AlterPartitionRequestData message = new AlterPartitionRequestData()
                    .setBrokerId(brokerId)
                    .setBrokerEpoch(brokerEpoch);
    
            inflightAlterPartitionItems.stream()
                    .collect(Collectors.groupingBy(item -> item.topicIdPartition().topicId()))
                    .forEach((topicId, items) -> {
                        AlterPartitionRequestData.TopicData topicData = new AlterPartitionRequestData.TopicData().setTopicId(topicId);
                        message.topics().add(topicData);
    
                        items.forEach(item -> {
                            AlterPartitionRequestData.PartitionData partitionData =
                                    new AlterPartitionRequestData.PartitionData()
                                            .setPartitionIndex(item.topicIdPartition().partitionId())
                                            .setLeaderEpoch(item.leaderAndIsr().leaderEpoch())
                                            .setNewIsrWithEpochs(item.leaderAndIsr().isrWithBrokerEpoch())
                                            .setPartitionEpoch(item.leaderAndIsr().partitionEpoch());
    
                            partitionData.setLeaderRecoveryState(item.leaderAndIsr().leaderRecoveryState().value());
    
                            topicData.partitions().add(partitionData);
                        });
    
                    });
    

            final var extractedValue = new AlterPartitionRequest.Builder(message);
            return extractedValue;
        }
}
