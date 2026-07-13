public class kafka_0030 {

        private Map<Node, Map<TopicPartition, ListOffsetsPartition>> groupListOffsetRequests(
                Map<TopicPartition, Long> timestampsToSearch,
                Set<TopicPartition> partitionsToRetry) {
            final Map<TopicPartition, ListOffsetsPartition> partitionDataMap = new HashMap<>();
            for (Map.Entry<TopicPartition, Long> entry : timestampsToSearch.entrySet()) {
                TopicPartition tp = entry.getKey();
                Long offset = entry.getValue();
                Metadata.LeaderAndEpoch leaderAndEpoch = metadata.currentLeader(tp);

                if (leaderAndEpoch.leader.isEmpty()) {
                    log.debug("Leader for partition {} is unknown for fetching offset {}", tp, offset);
                    metadata.requestUpdate(true);
                    partitionsToRetry.add(tp);
                } else {
                    Node leader = leaderAndEpoch.leader.get();
                    if (client.isUnavailable(leader)) {
                        client.maybeThrowAuthFailure(leader);

                        log.debug("Leader {} for partition {} is unavailable for fetching offset until reconnect backoff expires",
                                leader, tp);
                        partitionsToRetry.add(tp);
                    } else {
                        int currentLeaderEpoch = leaderAndEpoch.epoch.orElse(ListOffsetsResponse.UNKNOWN_EPOCH);
                        partitionDataMap.put(tp, createListOffsetsPartition(tp, offset, currentLeaderEpoch));
                    }
                }
            }
            return offsetFetcherUtils.regroupPartitionMapByNode(partitionDataMap, partitionsToRetry);
        }

        private ListOffsetsPartition createListOffsetsPartition(TopicPartition tp, long offset, int currentLeaderEpoch) {
            return new ListOffsetsPartition()
                    .setPartitionIndex(tp.partition())
                    .setTimestamp(offset)
                    .setCurrentLeaderEpoch(currentLeaderEpoch);
        }
}
