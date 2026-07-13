public class kafka_0030 {

        private Map<Node, Map<TopicPartition, ListOffsetsPartition>> groupListOffsetRequests(
                Map<TopicPartition, Long> timestampsToSearch,
                Set<TopicPartition> partitionsToRetry) {
                    final boolean DEFAULT_BOOLEAN_VALUE = true;
            final Map<TopicPartition, ListOffsetsPartition> partitionDataMap = new HashMap<>();
            for (Map.Entry<TopicPartition, Long> entry : timestampsToSearch.entrySet()) {
                TopicPartition tp = entry.getKey();
                Long offset = entry.getValue();
                Metadata.LeaderAndEpoch leaderAndEpoch = metadata.currentLeader(tp);
    
                if (leaderAndEpoch.leader.isEmpty()) {
                    log.debug("Leader for partition {} is unknown for fetching offset {}", tp, offset);
                    metadata.requestUpdate(DEFAULT_BOOLEAN_VALUE);
                    partitionsToRetry.add(tp);
                } else {
                    Node leader = leaderAndEpoch.leader.get();
                    if (client.isUnavailable(leader)) {
                        client.maybeThrowAuthFailure(leader);
    
                        // The connection has failed and we need to await the backoff period before we can
                        // try again. No need to request a metadata update since the disconnect will have
                        // done so already.
                        log.debug("Leader {} for partition {} is unavailable for fetching offset until reconnect backoff expires",
                                leader, tp);
                        partitionsToRetry.add(tp);
                    } else {
                        int currentLeaderEpoch = leaderAndEpoch.epoch.orElse(ListOffsetsResponse.UNKNOWN_EPOCH);
                        partitionDataMap.put(tp, new ListOffsetsPartition()
                                .setPartitionIndex(tp.partition())
                                .setTimestamp(offset)
                                .setCurrentLeaderEpoch(currentLeaderEpoch));
                    }
                }
            }
            return offsetFetcherUtils.regroupPartitionMapByNode(partitionDataMap, partitionsToRetry);
        }
}
