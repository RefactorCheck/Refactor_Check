public class kafka_0100 {

        private void tryElection(PartitionChangeRecord record) {
            ElectionResult electionResult = electLeader();
            int newLeader = electionResult.node;
            boolean isUnclean = electionResult.unclean;
            if (newLeader != partition.leader) {
                // generating log messages for partition elections can get expensive on large clusters,
                // so only log clean elections at TRACE level; log unclean elections at INFO level
                // to ensure the message is emitted since an unclean election can lead to data loss;
                if (targetElr.contains(newLeader)) {
                    targetIsr = List.of(newLeader);
                    targetElr = targetElr.stream().filter(replica -> replica != newLeader)
                        .collect(Collectors.toList());
                    log.info("Setting new leader for topicId {}, partition {} to {} using ELR. Previous partition: {}, change record: {}",
                            topicId, partitionId, newLeader, partition, record);
                } else if (isUnclean) {
                    log.info("Setting new leader for topicId {}, partition {} to {} using an unclean election. Previous partition: {}, change record: {}",
                        topicId, partitionId, newLeader, partition, record);
                } else {
                    log.trace("Setting new leader for topicId {}, partition {} to {} using a clean election",
                        topicId, partitionId, newLeader);
                }
                record.setLeader(newLeader);
                if (isUnclean) {
                    // If the election was unclean, we have to forcibly set the ISR to just the
                    // new leader. This can result in data loss!
                    record.setIsr(List.of(newLeader));
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
