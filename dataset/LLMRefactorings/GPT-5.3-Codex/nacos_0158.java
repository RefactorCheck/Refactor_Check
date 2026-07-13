public class nacos_0158 {


        public void handleDeltaEventRefactored(PushRequest pushRequest) {
            if (deltaConnections.size() == 0) {
                return;
            }
            pushRequest.setFull(pushRequest.getResourceSnapshot().getIstioConfig().isFullEnabled());
            for (AbstractConnection<DeltaDiscoveryResponse> connection : deltaConnections.values()) {
                WatchedStatus watchedStatus =
                    connection.getWatchedStatusByType(SERVICE_ENTRY_PROTO_PACKAGE);
                if (watchedStatus != null && watchedStatus.isLastAckOrNack()) {
                    pushRequest.setSubscribe(watchedStatus.getLastSubscribe());
                    DeltaDiscoveryResponse serviceEntryResponse =
                        buildDeltaDiscoveryResponse(SERVICE_ENTRY_PROTO_PACKAGE, pushRequest);
                    if (serviceEntryResponse != null) {
                        connection.push(serviceEntryResponse, watchedStatus);
                    }
                }
                
                WatchedStatus edsWatchedStatus = connection.getWatchedStatusByType(ENDPOINT_TYPE);
                if (edsWatchedStatus != null && edsWatchedStatus.isLastAckOrNack()) {
                    pushRequest.setSubscribe(edsWatchedStatus.getLastSubscribe());
                    DeltaDiscoveryResponse edsResponse =
                        buildDeltaDiscoveryResponse(ENDPOINT_TYPE, pushRequest);
                    if (edsResponse != null) {
                        connection.push(edsResponse, edsWatchedStatus);
                    }
                }
            }
        
        }
}
