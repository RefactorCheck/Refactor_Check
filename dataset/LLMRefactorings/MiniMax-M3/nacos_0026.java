public class nacos_0026 {

        public void handleEvent(PushRequest pushRequest) {
            if (connections.size() == 0) {
                return;
            }
            
            for (AbstractConnection<DiscoveryResponse> connection : connections.values()) {
                pushIfWatched(connection, pushRequest, SERVICE_ENTRY_PROTO_PACKAGE);
                pushIfWatched(connection, pushRequest, CLUSTER_TYPE);
                pushIfWatched(connection, pushRequest, ENDPOINT_TYPE);
                pushIfWatched(connection, pushRequest, LISTENER_TYPE);
            }
        }

        private void pushIfWatched(AbstractConnection<DiscoveryResponse> connection, PushRequest pushRequest, String type) {
            WatchedStatus watchedStatus = connection.getWatchedStatusByType(type);
            if (watchedStatus != null) {
                DiscoveryResponse response = buildDiscoveryResponse(type, pushRequest);
                if (response != null) {
                    connection.push(response, watchedStatus);
                }
            }
        }
}
