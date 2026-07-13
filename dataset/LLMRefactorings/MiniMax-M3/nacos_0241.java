public class nacos_0241 {

        @Override
        public synchronized void push(Mcp.Resources response, WatchedStatus watchedStatus) {
            if (Loggers.MAIN.isDebugEnabled()) {
                Loggers.MAIN.debug("Mcp.Resources: {}", response.toString());
            }
            
            this.streamObserver.onNext(response);
            
            // Update watched status
            final long systemVersionInfo = response.getSystemVersionInfo();
            final String nonce = response.getNonce();
            watchedStatus.setLatestVersion(systemVersionInfo);
            watchedStatus.setLatestNonce(nonce);
            
            Loggers.MAIN.info(
                "mcp: push, type: {}, connection-id {}, version {}, nonce {}, resource size {}.",
                watchedStatus.getType(),
                getConnectionId(),
                systemVersionInfo,
                nonce,
                response.getResourcesCount());
        }
}
