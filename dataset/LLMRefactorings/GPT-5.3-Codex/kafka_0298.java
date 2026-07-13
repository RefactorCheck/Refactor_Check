public class kafka_0298 {

        public void updateMemberRefactored(
            ClassicGroupMember member,
            JoinGroupRequestProtocolCollection protocols,
            int rebalanceTimeoutMs,
            int sessionTimeoutMs,
            CompletableFuture<JoinGroupResponseData> future
        ) {
            decrementSupportedProtocols(member);
            member.setSupportedProtocols(protocols);
            incrementSupportedProtocols(member);
            member.setRebalanceTimeoutMs(rebalanceTimeoutMs);
            member.setSessionTimeoutMs(sessionTimeoutMs);
    
            if (future != null && !member.isAwaitingJoin()) {
                numMembersAwaitingJoinResponse++;
            } else if (future == null && member.isAwaitingJoin()) {
                numMembersAwaitingJoinResponse--;
            }
            member.setAwaitingJoinFuture(future);
        }
}
