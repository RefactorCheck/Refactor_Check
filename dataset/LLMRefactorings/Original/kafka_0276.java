public class kafka_0276 {

        protected void close(Timer timer, CloseOptions.GroupMembershipOperation membershipOperation) {
            try {
                closeHeartbeatThread();
            } finally {
                // Synchronize after closing the heartbeat thread since heartbeat thread
                // needs this lock to complete and terminate after close flag is set.
                synchronized (this) {
                    onLeavePrepare();
                    maybeLeaveGroup(membershipOperation, "the consumer is being closed");
    
                    // At this point, there may be pending commits (async commits or sync commits that were
                    // interrupted using wakeup) and the leave group request which have been queued, but not
                    // yet sent to the broker. Wait up to close timeout for these pending requests to be processed.
                    // If coordinator is not known, requests are aborted.
                    Node coordinator = checkAndGetCoordinator();
                    if (coordinator != null && !client.awaitPendingRequests(coordinator, timer))
                        log.warn("Close timed out with {} pending requests to coordinator, terminating client connections",
                                client.pendingRequestCount(coordinator));
                }
            }
        }
}
