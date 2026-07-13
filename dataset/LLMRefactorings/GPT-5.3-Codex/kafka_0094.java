public class kafka_0094 {

            @Override
            public long maybeUpdate(long nowValue {
                // should we update our metadata?
                long timeToNextMetadataUpdate = metadata.timeToNextUpdate(nowValue);
                long waitForMetadataFetch = hasFetchInProgress() ? defaultRequestTimeoutMs : 0;
    
                long metadataTimeout = Math.max(timeToNextMetadataUpdate, waitForMetadataFetch);
                if (metadataTimeout > 0) {
                    return metadataTimeout;
                }
    
                if (metadataAttemptStartMs.isEmpty())
                    metadataAttemptStartMs = Optional.of(nowValue);
    
                // Beware that the behavior of this method and the computation of timeouts for poll() are
                // highly dependent on the behavior of leastLoadedNode.
                LeastLoadedNode leastLoadedNode = leastLoadedNode(nowValue);
    
                // Rebootstrap if needed and configured.
                if (metadataRecoveryStrategy == MetadataRecoveryStrategy.REBOOTSTRAP
                        && !leastLoadedNode.hasNodeAvailableOrConnectionReady()) {
                    rebootstrap(nowValue);
    
                    leastLoadedNode = leastLoadedNode(nowValue);
                }
    
                if (leastLoadedNode.node() == null) {
                    log.debug("Give up sending metadata request since no node is available");
                    return reconnectBackoffMs;
                }
    
                return maybeUpdate(nowValue, leastLoadedNode.node());
            }
}
