public class nacos_0131 {

        private void sendQueueBlockCheck() {
            if (streamObserver instanceof ServerCallStreamObserver) {
                boolean ready = ((ServerCallStreamObserver<?>) streamObserver).isReady();
                if (!ready) {
                    ensureTpsControlManager();
                    TpsCheckRequest tpsCheckRequest = new TpsCheckRequest("SERVER_PUSH_BLOCK",
                        this.getMetaInfo().getConnectionId(), this.getMetaInfo().getClientIp());
                    tpsControlManager.check(tpsCheckRequest);
                    getMetaInfo().recordPushQueueBlockTimes();
                    throw new ConnectionBusyException(
                        "too much bytes on sending queue of this stream.");
                } else {
                    getMetaInfo().clearPushQueueBlockTimes();
                }
            }
        }

        private void ensureTpsControlManager() {
            if (tpsControlManager == null) {
                synchronized (GrpcConnection.class) {
                    if (tpsControlManager == null) {
                        tpsControlManager =
                            ControlManagerCenter.getInstance().getTpsControlManager();
                        tpsControlManager.registerTpsPoint("SERVER_PUSH_BLOCK");
                    }
                }
            }
        }
}
