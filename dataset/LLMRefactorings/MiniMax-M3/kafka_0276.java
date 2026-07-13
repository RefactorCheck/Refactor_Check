public class kafka_0276 {

    private static final String CONSUMER_BEING_CLOSED_MESSAGE = "the consumer is being closed";

    protected void close(Timer timer, CloseOptions.GroupMembershipOperation membershipOperation) {
        try {
            closeHeartbeatThread();
        } finally {
            synchronized (this) {
                onLeavePrepare();
                maybeLeaveGroup(membershipOperation, CONSUMER_BEING_CLOSED_MESSAGE);

                Node coordinator = checkAndGetCoordinator();
                if (coordinator != null && !client.awaitPendingRequests(coordinator, timer))
                    log.warn("Close timed out with {} pending requests to coordinator, terminating client connections",
                            client.pendingRequestCount(coordinator));
            }
        }
    }
}
