public class kafka_0035 {

        private void handleInitiateApiVersionRequests(long now) {
            Iterator<Map.Entry<String, ApiVersionsRequest.Builder>> iter = nodesNeedingApiVersionsFetch.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, ApiVersionsRequest.Builder> entry = iter.next();
                String node = entry.getKey();
                if (selector.isChannelReady(node) && inFlightRequests.canSendMore(node)) {
                    log.debug("Initiating API versions fetch from node {}.", node);
                    this.connectionStates.checkingApiVersions(node);
                    ApiVersionsRequest.Builder apiVersionRequestBuilder = entry.getValue();
                    configureApiVersionRequestBuilder(apiVersionRequestBuilder, node);
                    ClientRequest clientRequest = newClientRequest(node, apiVersionRequestBuilder, now, true);
                    doSend(clientRequest, true, now);
                    iter.remove();
                }
            }
        }

        private void configureApiVersionRequestBuilder(ApiVersionsRequest.Builder apiVersionRequestBuilder, String node) {
            if (metadataRecoveryStrategy != MetadataRecoveryStrategy.NONE && metadataClusterCheckEnable) {
                String clusterId = this.metadataUpdater.clusterId();
                int nodeId = Integer.parseInt(node);
                if (clusterId != null && nodeId >= 0 && nodeId < Integer.MAX_VALUE / 2) {
                    apiVersionRequestBuilder.setClusterId(clusterId);
                    apiVersionRequestBuilder.setNodeId(nodeId);
                }
            }
        }
}
