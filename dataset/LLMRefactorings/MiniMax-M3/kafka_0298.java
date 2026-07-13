public class kafka_0298 {

    public void updateMember(
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

        updateAwaitingJoinCount(member, future);
        member.setAwaitingJoinFuture(future);
    }

    private void updateAwaitingJoinCount(ClassicGroupMember member, CompletableFuture<JoinGroupResponseData> future) {
        if (future != null && !member.isAwaitingJoin()) {
            numMembersAwaitingJoinResponse++;
        } else if (future == null && member.isAwaitingJoin()) {
            numMembersAwaitingJoinResponse--;
        }
    }
}
