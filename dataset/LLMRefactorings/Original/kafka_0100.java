public class kafka_0100 {

        private void tryElection(PartitionChangeRecord record) {
            ElectionResult electionResult = electLeader();
            if (electionResult.node != partition.leader) {
                // generating log messages for partition elections can get expensive on large clusters,
                // so only log clean elections at TRACE level; log unclean elections at INFO level
                // to ensure the message is emitted since an unclean election can lead to data loss;
                if (targetElr.contains(electionResult.node)) {
                    targetIsr = List.of(electionResult.node);
                    targetElr = targetElr.stream().filter(replica -> replica != electionResult.node)
                        .collect(Collectors.toList());
                    log.info("Setting new leader for topicId {}, partition {} to {} using ELR. Previous partition: {}, change record: {}",
                            topicId, partitionId, electionResult.node, partition, record);
                } else if (electionResult.unclean) {
                    log.info("Setting new leader for topicId {}, partition {} to {} using an unclean election. Previous partition: {}, change record: {}",
                        topicId, partitionId, electionResult.node, partition, record);
                } else {
                    log.trace("Setting new leader for topicId {}, partition {} to {} using a clean election",
                        topicId, partitionId, electionResult.node);
                }
                record.setLeader(electionResult.node);
                if (electionResult.unclean) {
                    // If the election was unclean, we have to forcibly set the ISR to just the
                    // new leader. This can result in data loss!
                    record.setIsr(List.of(electionResult.node));
                    if (partition.leaderRecoveryState != LeaderRecoveryState.RECOVERING) {
                        // And mark the leader recovery state as RECOVERING
                        record.setLeaderRecoveryState(LeaderRecoveryState.RECOVERING.value());
                    }
                }
            } else {
                log.trace("Failed to find a new leader with current state: {}", this);
            }
        }
}
