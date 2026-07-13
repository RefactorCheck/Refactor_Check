public class kafka_0030 {

        private Map<Node, Map<TopicPartition, ListOffsetsPartition>> groupListOffsetRequests(
                Map<TopicPartition, Long> timestampsToSearch,
                Set<TopicPartition> partitionsToRetry) {
            final Map<TopicPartition, ListOffsetsPartition> partitionDataMap = new HashMap<>();
            for (Map.Entry<TopicPartition, Long> entry : timestampsToSearch.entrySet()) {
                TopicPartition topicPartition = entry.getKey();
                Long offset = entry.getValue();
                Metadata.LeaderAndEpoch leaderAndEpoch = metadata.currentLeader(topicPartition);
    
                if (leaderAndEpoch.leader.isEmpty()) {
                    log.debug("Leader for partition {} is unknown for fetching offset {}", topicPartition, offset);
                    metadata.requestUpdate(true);
                    partitionsToRetry.add(topicPartition);
                } else {
                    Node leader = leaderAndEpoch.leader.get();
                    if (client.isUnavailable(leader)) {
                        client.maybeThrowAuthFailure(leader);
    
                        // The connection has failed and we need to await the backoff period before we can
                        // try again. No need to request a metadata update since the disconnect will have
                        // done so already.
                        log.debug("Leader {} for partition {} is unavailable for fetching offset until reconnect backoff expires",
                                leader, topicPartition);
                        partitionsToRetry.add(topicPartition);
                    } else {
                        int currentLeaderEpoch = leaderAndEpoch.epoch.orElse(ListOffsetsResponse.UNKNOWN_EPOCH);
                        partitionDataMap.put(topicPartition, new ListOffsetsPartition()
                                .setPartitionIndex(topicPartition.partition())
                                .setTimestamp(offset)
                                .setCurrentLeaderEpoch(currentLeaderEpoch));
                    }
                }
            }
            return offsetFetcherUtils.regroupPartitionMapByNode(partitionDataMap, partitionsToRetry);
        }
}
