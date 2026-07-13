public class nacos_0026 {

        public void handleEventRefactored(PushRequest pushRequest) {
            if (connections.size() == 0) {
                return;
            }
            
            for (AbstractConnection<DiscoveryResponse> connection : connections.values()) {
                //mcp
                WatchedStatus watchedStatus =
                    connection.getWatchedStatusByType(SERVICE_ENTRY_PROTO_PACKAGE);
                if (watchedStatus != null) {
                    DiscoveryResponse serviceEntryResponse =
                        buildDiscoveryResponse(SERVICE_ENTRY_PROTO_PACKAGE, pushRequest);
                    connection.push(serviceEntryResponse, watchedStatus);
                }
                //CDS
                WatchedStatus cdsWatchedStatus = connection.getWatchedStatusByType(CLUSTER_TYPE);
                if (cdsWatchedStatus != null) {
                    DiscoveryResponse cdsResponse = buildDiscoveryResponse(CLUSTER_TYPE, pushRequest);
                    if (cdsResponse != null) {
                        connection.push(cdsResponse, cdsWatchedStatus);
                    }
                }
                //EDS
                WatchedStatus edsWatchedStatus = connection.getWatchedStatusByType(ENDPOINT_TYPE);
                if (edsWatchedStatus != null) {
                    DiscoveryResponse edsResponse = buildDiscoveryResponse(ENDPOINT_TYPE, pushRequest);
                    connection.push(edsResponse, edsWatchedStatus);
                }
                //LDS
                WatchedStatus ldsWatchedStatus = connection.getWatchedStatusByType(LISTENER_TYPE);
                if (ldsWatchedStatus != null) {
                    DiscoveryResponse ldsResponse = buildDiscoveryResponse(LISTENER_TYPE, pushRequest);
                    connection.push(ldsResponse, ldsWatchedStatus);
                }
                
            }
        }
}
