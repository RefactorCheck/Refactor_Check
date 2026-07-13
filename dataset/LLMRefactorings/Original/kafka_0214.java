public class kafka_0214 {

        private void processDisconnection(List<ClientResponse> responses,
                                          String nodeId,
                                          long now,
                                          ChannelState disconnectState,
                                          boolean timedOut) {
            connectionStates.disconnected(nodeId, now);
            apiVersions.remove(nodeId);
            nodesNeedingApiVersionsFetch.remove(nodeId);
            switch (disconnectState.state()) {
                case AUTHENTICATION_FAILED:
                    AuthenticationException exception = disconnectState.exception();
                    connectionStates.authenticationFailed(nodeId, now, exception);
                    log.error("Connection to node {} ({}) failed authentication due to: {}", nodeId,
                        disconnectState.remoteAddress(), exception.getMessage());
                    break;
                case AUTHENTICATE:
                    log.warn("Connection to node {} ({}) terminated during authentication. This may happen " +
                        "due to any of the following reasons: (1) Firewall blocking Kafka TLS " +
                        "traffic (eg it may only allow HTTPS traffic), (2) Transient network issue.",
                        nodeId, disconnectState.remoteAddress());
                    break;
                case NOT_CONNECTED:
                    log.warn("Connection to node {} ({}) could not be established. Node may not be available.", nodeId, disconnectState.remoteAddress());
                    break;
                default:
                    break; // Disconnections in other states are logged at debug level in Selector
            }
    
            cancelInFlightRequests(nodeId, now, responses, timedOut);
            metadataUpdater.handleServerDisconnect(now, nodeId, Optional.ofNullable(disconnectState.exception()));
        }
}
