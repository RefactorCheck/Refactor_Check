public class kafka_0215 {

        void touch(int brokerId, boolean fenced, long metadataOffset) {
            BrokerHeartbeatState broker = heartbeatStateOrThrow(brokerId);
            // Remove the broker from the unfenced list and/or the active set. Its
            // position in either of those data structures depends on values we are
            // changing here. We will re-add it if necessary at the end of this function.
            untrack(broker);
            broker.fenced = fenced;
            broker.metadataOffset = metadataOffset;
            boolean shouldBeActive = false;
            if (fenced) {
                // If a broker is fenced, it leaves controlled shutdown.  On its next heartbeat,
                // it will shut down immediately.
                broker.controlledShutdownOffset = -1;
            } else {
                if (!broker.shuttingDown()) {
                    shouldBeActive = true;
                }
            }
            if (shouldBeActive) {
                active.add(broker);
            } else {
                active.remove(broker);
            }
        }
}
