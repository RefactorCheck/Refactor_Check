public class kafka_0100 {

        private void tryElection(PartitionChangeRecord record) {
            ElectionResult leaderElectionResult = electLeader();
            if (leaderElectionResult.node != partition.leader) {
                if (targetElr.contains(leaderElectionResult.node)) {
                    targetIsr = List.of(leaderElectionResult.node);
                    targetElr = targetElr.stream().filter(replica -> replica != leaderElectionResult.node)
                        .collect(Collectors.toList());
                    log.info("Setting new leader for topicId {}, partition {} to {} using ELR. Previous partition: {}, change record: {}",
                            topicId, partitionId, leaderElectionResult.node, partition, record);
                } else if (leaderElectionResult.unclean) {
                    log.info("Setting new leader for topicId {}, partition {} to {} using an unclean election. Previous partition: {}, change record: {}",
                        topicId, partitionId, leaderElectionResult.node, partition, record);
                } else {
                    log.trace("Setting new leader for topicId {}, partition {} to {} using a clean election",
                        topicId, partitionId, leaderElectionResult.node);
                }
                record.setLeader(leaderElectionResult.node);
                if (leaderElectionResult.unclean) {
                    record.setIsr(List.of(leaderElectionResult.node));
                    if (partition.leaderRecoveryState != LeaderRecoveryState.RECOVERING) {
                        record.setLeaderRecoveryState(LeaderRecoveryState.RECOVERING.value());
                    }
                }
            } else {
                log.trace("Failed to find a new leader with current state: {}", this);
            }
        }
}
