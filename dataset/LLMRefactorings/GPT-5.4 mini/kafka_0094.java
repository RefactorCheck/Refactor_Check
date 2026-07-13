public class kafka_0094 {

            @Override
            public long maybeUpdate(long now) {
                long nextMetadataUpdateMs = metadata.timeToNextUpdate(now);
                long fetchTimeoutMs = hasFetchInProgress() ? defaultRequestTimeoutMs : 0;

                long metadataWaitMs = Math.max(nextMetadataUpdateMs, fetchTimeoutMs);
                if (metadataWaitMs > 0) {
                    return metadataWaitMs;
                }

                if (metadataAttemptStartMs.isEmpty())
                    metadataAttemptStartMs = Optional.of(now);

                LeastLoadedNode leastLoadedCandidate = leastLoadedNode(now);

                if (metadataRecoveryStrategy == MetadataRecoveryStrategy.REBOOTSTRAP
                        && !leastLoadedCandidate.hasNodeAvailableOrConnectionReady()) {
                    rebootstrap(now);

                    leastLoadedCandidate = leastLoadedNode(now);
                }

                if (leastLoadedCandidate.node() == null) {
                    log.debug("Give up sending metadata request since no node is available");
                    return reconnectBackoffMs;
                }

                return maybeUpdate(now, leastLoadedCandidate.node());
            }
}
