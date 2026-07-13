public class kafka_0056 {

        RequestFuture<ByteBuffer> sendJoinGroupRequest() {
            if (coordinatorUnknown())
                return RequestFuture.coordinatorNotAvailable();
    
            // send a join group request to the coordinator
            log.info("(Re-)joining group");
            JoinGroupRequest.Builder requestBuilder = new JoinGroupRequest.Builder(
                    new JoinGroupRequestData()
                            .setGroupId(rebalanceConfig.groupId)
                            .setSessionTimeoutMs(this.rebalanceConfig.sessionTimeoutMs)
                            .setMemberId(this.generation.memberId)
                            .setGroupInstanceId(this.rebalanceConfig.groupInstanceId.orElse(null))
                            .setProtocolType(protocolType())
                            .setProtocols(metadata())
                            .setRebalanceTimeoutMs(this.rebalanceConfig.rebalanceTimeoutMs)
                            .setReason(JoinGroupRequest.maybeTruncateReason(this.rejoinReason))
            );
    
            log.debug("Sending JoinGroup ({}) to coordinator {}", requestBuilder, this.coordinator);
    
            // Note that we override the request timeout using the rebalance timeout since that is the
            // maximum time that it may block on the coordinator. We add an extra 5 seconds for small delays.
            int joinGroupTimeoutMs = Math.max(
                client.defaultRequestTimeoutMs(),
                Math.max(
                    rebalanceConfig.rebalanceTimeoutMs + JOIN_GROUP_TIMEOUT_LAPSE,
                    rebalanceConfig.rebalanceTimeoutMs) // guard against overflow since rebalance timeout can be MAX_VALUE
                );
            return client.send(coordinator, requestBuilder, joinGroupTimeoutMs)
                    .compose(new JoinGroupResponseHandler(generation));
        }
}
