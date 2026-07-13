public class kafka_0143 {

        private boolean canElectLastKnownLeader() {
            if (!eligibleLeaderReplicasEnabled || !useLastKnownLeaderInBalancedRecovery) {
                log.trace("Try to elect last known leader for {}-{} but elrEnabled={}, useLastKnownLeaderInBalancedRecovery={}",
                        topicId, partitionId, eligibleLeaderReplicasEnabled, useLastKnownLeaderInBalancedRecovery);
                return false;
            }
            if (!targetElr.isEmpty() || !targetIsr.isEmpty()) {
                log.trace("Try to elect last known leader for {}-{} but ELR/ISR is not empty. ISR={}, ELR={}",
                        topicId, partitionId, targetIsr, targetElr);
                return false;
            }
    
            // When the last known leader is enabled:
            // 1. The targetLastKnownElr will only be used to store the last known leader, and it is updated after the
            //    leader election. So we can only refer to the lastKnownElr in the existing partition registration.
            // 2. When useLastKnownLeaderInBalancedRecovery=false, it intends to use other type of unclean leader election
            //    and the lastKnownElr is populated with the real last known ELR members. Then it may have multiple members
            //    in the field even if useLastKnownLeaderInBalancedRecovery is set to true again. In this case, we can't
            //    refer to the lastKnownElr.
            if (partition.lastKnownElr.length != 1) {
                log.trace("Try to elect last known leader for {}-{} but lastKnownElr does not only have 1 member. lastKnownElr={}",
                        topicId, partitionId, Arrays.toString(partition.lastKnownElr));
                return false;
            }
            if (!isAcceptableLeader.test(partition.lastKnownElr[0])) {
                log.trace("Try to elect last known leader for {}-{} but last known leader is not alive. last known leader={}",
                        topicId, partitionId, partition.lastKnownElr[0]);
                return false;
            }
            return true;
        }
}
