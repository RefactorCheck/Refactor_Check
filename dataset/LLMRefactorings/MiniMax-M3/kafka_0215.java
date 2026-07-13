public class kafka_0215 {

        void touch(int brokerId, boolean fenced, long metadataOffset) {
            BrokerHeartbeatState broker = heartbeatStateOrThrow(brokerId);
            untrack(broker);
            broker.fenced = fenced;
            broker.metadataOffset = metadataOffset;
            if (fenced) {
                broker.controlledShutdownOffset = -1;
            }
            if (!fenced && !broker.shuttingDown()) {
                active.add(broker);
            } else {
                active.remove(broker);
            }
        }
}
