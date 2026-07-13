public class kafka_0035 {

        private void handleInitiateApiVersionRequests(long now) {
            Iterator<Map.Entry<String, ApiVersionsRequest.Builder>> iter = nodesNeedingApiVersionsFetch.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, ApiVersionsRequest.Builder> entry = iter.next();
                String node = entry.getKey();
                if (selector.isChannelReady(node) && inFlightRequests.canSendMore(node)) {
                    log.debug("Initiating API versions fetch from node {}.", node);
                    // We transition the connection to the CHECKING_API_VERSIONS state only when
                    // the ApiVersionsRequest is queued up to be sent out. Without this, the client
                    // could remain in the CHECKING_API_VERSIONS state forever if the channel does
                    // not before ready.
                    this.connectionStates.checkingApiVersions(node);
                    ApiVersionsRequest.Builder apiVersionRequestBuilder = entry.getValue();
                    // If we know the cluster ID and node ID we are connecting to, we can include
                    // those details in the ApiVersions request for checking in the broker,
                    // provided that the metadata recovery strategy is not NONE. (KIP-1242)
                    if (metadataRecoveryStrategy != MetadataRecoveryStrategy.NONE && metadataClusterCheckEnable) {
                        String clusterId = this.metadataUpdater.clusterId();
                        int nodeId = Integer.parseInt(node);
                        // In order to allow separate connections to coordinators, the client uses large positive node ID values
                        // (Integer.MAX_VALUE - nodeId) for these connections which do not match the target broker's actual node ID.
                        // To avoid those, only check if the node ID is less than half of Integer.MAX_VALUE.
                        if (clusterId != null && nodeId >= 0 && nodeId < Integer.MAX_VALUE / 2) {
                            apiVersionRequestBuilder.setClusterId(clusterId);
                            apiVersionRequestBuilder.setNodeId(nodeId);
                        }
                    }
                    ClientRequest clientRequest = newClientRequest(node, apiVersionRequestBuilder, now, true);
                    doSend(clientRequest, true, now);
                    iter.remove();
                }
            }
        }
}
