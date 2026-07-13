public class kafka_0094 {

            @Override
            public long maybeUpdate(long now) {
                long metadataTimeout = computeMetadataTimeout(now);
                if (metadataTimeout > 0) {
                    return metadataTimeout;
                }
    
                if (metadataAttemptStartMs.isEmpty())
                    metadataAttemptStartMs = Optional.of(now);
    
                // Beware that the behavior of this method and the computation of timeouts for poll() are
                // highly dependent on the behavior of leastLoadedNode.
                LeastLoadedNode leastLoadedNode = leastLoadedNode(now);
    
                // Rebootstrap if needed and configured.
                if (metadataRecoveryStrategy == MetadataRecoveryStrategy.REBOOTSTRAP
                        && !leastLoadedNode.hasNodeAvailableOrConnectionReady()) {
                    rebootstrap(now);
    
                    leastLoadedNode = leastLoadedNode(now);
                }
    
                if (leastLoadedNode.node() == null) {
                    log.debug("Give up sending metadata request since no node is available");
                    return reconnectBackoffMs;
                }
    
                return maybeUpdate(now, leastLoadedNode.node());
            }
    
            private long computeMetadataTimeout(long now) {
                long timeToNextMetadataUpdate = metadata.timeToNextUpdate(now);
                long waitForMetadataFetch = hasFetchInProgress() ? defaultRequestTimeoutMs : 0;
                return Math.max(timeToNextMetadataUpdate, waitForMetadataFetch);
            }
}
