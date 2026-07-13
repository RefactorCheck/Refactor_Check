public class kafka_0143 {

    private boolean canElectLastKnownLeader() {
        if (!eligibleLeaderReplicasEnabled || !useLastKnownLeaderInBalancedRecovery) {
            return cannotElectLastKnownLeader("elrEnabled={}, useLastKnownLeaderInBalancedRecovery={}",
                    eligibleLeaderReplicasEnabled, useLastKnownLeaderInBalancedRecovery);
        }
        if (!targetElr.isEmpty() || !targetIsr.isEmpty()) {
            return cannotElectLastKnownLeader("ELR/ISR is not empty. ISR={}, ELR={}", targetIsr, targetElr);
        }
        // When the last known leader is enabled:
        // 1. The targetLastKnownElr will only be used to store the last known leader, and it is updated after the
        //    leader election. So we can only refer to the lastKnownElr in the existing partition registration.
        // 2. When useLastKnownLeaderInBalancedRecovery=false, it intends to use other type of unclean leader election
        //    and the lastKnownElr is populated with the real last known ELR members. Then it may have multiple members
        //    in the field even if useLastKnownLeaderInBalancedRecovery is set to true again. In this case, we can't
        //    refer to the lastKnownElr.
        if (partition.lastKnownElr.length != 1) {
            return cannotElectLastKnownLeader("lastKnownElr does not only have 1 member. lastKnownElr={}",
                    Arrays.toString(partition.lastKnownElr));
        }
        if (!isAcceptableLeader.test(partition.lastKnownElr[0])) {
            return cannotElectLastKnownLeader("last known leader is not alive. last known leader={}",
                    partition.lastKnownElr[0]);
        }
        return true;
    }

    private boolean cannotElectLastKnownLeader(String reason, Object... reasonArgs) {
        Object[] allArgs = new Object[reasonArgs.length + 2];
        allArgs[0] = topicId;
        allArgs[1] = partitionId;
        System.arraycopy(reasonArgs, 0, allArgs, 2, reasonArgs.length);
        log.trace("Try to elect last known leader for {}-{} but " + reason, allArgs);
        return false;
    }
}
