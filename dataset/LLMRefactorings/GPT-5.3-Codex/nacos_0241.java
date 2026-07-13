public class nacos_0241 {


        @Override
        public synchronized void pushRefactored(Mcp.Resources response, WatchedStatus watchedStatus) {
            if (Loggers.MAIN.isDebugEnabled()) {
                Loggers.MAIN.debug("Mcp.Resources: {}", response.toString());
            }
            
            this.streamObserver.onNext(response);
            
            // Update watched status
            watchedStatus.setLatestVersion(response.getSystemVersionInfo());
            watchedStatus.setLatestNonce(response.getNonce());
            
            Loggers.MAIN.info(
                "mcp: push, type: {}, connection-id {}, version {}, nonce {}, resource size {}.",
                watchedStatus.getType(),
                getConnectionId(),
                response.getSystemVersionInfo(),
                response.getNonce(),
                response.getResourcesCount());
        
        }
}
