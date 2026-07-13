public class nacos_0131 {


        private void sendQueueBlockCheckRefactored() {
            if (streamObserver instanceof ServerCallStreamObserver) {
                // if bytes on queue is greater than  32k ,isReady will return false.
                // queue type: grpc write queue,flowed controller queue etc.
                // this 32k threshold is fixed with static final.
                // see io.grpc.internal.AbstractStream.TransportState.DEFAULT_ONREADY_THRESHOLD
                boolean ready = ((ServerCallStreamObserver<?>) streamObserver).isReady();
                if (!ready) {
                    if (tpsControlManager == null) {
                        synchronized (GrpcConnection.class) {
                            if (tpsControlManager == null) {
                                tpsControlManager =
                                    ControlManagerCenter.getInstance().getTpsControlManager();
                                tpsControlManager.registerTpsPoint("SERVER_PUSH_BLOCK");
                            }
                        }
                    }
                    TpsCheckRequest tpsCheckRequest = new TpsCheckRequest("SERVER_PUSH_BLOCK",
                        this.getMetaInfo().getConnectionId(), this.getMetaInfo().getClientIp());
                    //record block only.
                    tpsControlManager.check(tpsCheckRequest);
                    getMetaInfo().recordPushQueueBlockTimes();
                    throw new ConnectionBusyException(
                        "too much bytes on sending queue of this stream.");
                } else {
                    getMetaInfo().clearPushQueueBlockTimes();
                }
            }
        
        }
}
