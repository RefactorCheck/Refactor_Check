public class kafka_0012 {

        private void maybeUpdateRecordElr(PartitionChangeRecord record) {
            // During the leader election, it can set the record isr if an unclean leader election happens.
            boolean isCleanElection = record.isr() == null;

            // Clean the ELR related fields if it is an unclean election or ELR is disabled.
            if (!isCleanElection || !eligibleLeaderReplicasEnabled) {
                targetElr = List.of();
                targetLastKnownElr = List.of();
            }

            if (!targetElr.equals(Replicas.toList(partition.elr))) {
                record.setEligibleLeaderReplicas(targetElr);
            }

            if (useLastKnownLeaderInBalancedRecovery && partition.lastKnownElr.length == 1 &&
                    (record.leader() == NO_LEADER || record.leader() == NO_LEADER_CHANGE && partition.leader == NO_LEADER)) {
                // If the last known leader is stored in the lastKnownElr, the last known elr should not be updated when
                // the partition does not have a leader.
                targetLastKnownElr = Replicas.toList(partition.lastKnownElr);
            }

            // If the last known ELR is expected to store the last known leader, the lastKnownElr field should be updated
            // later in maybeUpdateLastKnownLeader.
            if (useLastKnownLeaderInBalancedRecovery) return;

            if (!targetLastKnownElr.equals(Replicas.toList(partition.lastKnownElr))) {
                record.setLastKnownElr(targetLastKnownElr);
            }
        }
}
