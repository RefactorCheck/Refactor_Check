public class nacos_0158 {

        public void handleDeltaEvent(PushRequest pushRequest) {
            if (deltaConnections.size() == 0) {
                return;
            }
            pushRequest.setFull(pushRequest.getResourceSnapshot().getIstioConfig().isFullEnabled());
            for (AbstractConnection<DeltaDiscoveryResponse> connection : deltaConnections.values()) {
                pushDeltaResponse(connection, pushRequest, SERVICE_ENTRY_PROTO_PACKAGE);
                pushDeltaResponse(connection, pushRequest, ENDPOINT_TYPE);
            }
        }

        private void pushDeltaResponse(AbstractConnection<DeltaDiscoveryResponse> connection, PushRequest pushRequest, String type) {
            WatchedStatus watchedStatus = connection.getWatchedStatusByType(type);
            if (watchedStatus != null && watchedStatus.isLastAckOrNack()) {
                pushRequest.setSubscribe(watchedStatus.getLastSubscribe());
                DeltaDiscoveryResponse response = buildDeltaDiscoveryResponse(type, pushRequest);
                if (response != null) {
                    connection.push(response, watchedStatus);
                }
            }
        }
}
