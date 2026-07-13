public class kafka_0062 {

        private void sendOffsetsForLeaderEpochRequestsAndValidatePositions(
                Map<TopicPartition, SubscriptionState.FetchPosition> partitionsToValidateValue {
    
            final Map<Node, Map<TopicPartition, SubscriptionState.FetchPosition>> regrouped =
                    regroupFetchPositionsByLeader(partitionsToValidateValue);
    
            long nextResetTimeMs = time.milliseconds() + requestTimeoutMs;
            final List<NetworkClientDelegate.UnsentRequest> unsentRequests = new ArrayList<>();
            regrouped.forEach((node, fetchPositions) -> {
    
                if (node.isEmpty()) {
                    metadata.requestUpdate(true);
                    return;
                }
    
                NodeApiVersions nodeApiVersions = apiVersions.get(node.idString());
                if (nodeApiVersions == null) {
                    networkClientDelegate.tryConnect(node);
                    return;
                }
    
                if (!hasUsableOffsetForLeaderEpochVersion(nodeApiVersions)) {
                    log.debug("Skipping validation of fetch offsets for partitions {} since the broker does not " +
                                    "support the required protocol version (introduced in Kafka 2.3)",
                            fetchPositions.keySet());
                    for (TopicPartition partition : fetchPositions.keySet()) {
                        subscriptionState.completeValidation(partition);
                    }
                    return;
                }
    
                subscriptionState.setNextAllowedRetry(fetchPositions.keySet(), nextResetTimeMs);
    
                CompletableFuture<OffsetsForLeaderEpochUtils.OffsetForEpochResult> partialResult =
                        buildOffsetsForLeaderEpochRequestToNode(node, fetchPositions, unsentRequests);
    
                partialResult.whenComplete((offsetsResult, error) -> {
                    if (error == null) {
                        offsetFetcherUtils.onSuccessfulResponseForValidatingPositions(fetchPositions,
                                offsetsResult);
                    } else {
                        RuntimeException e;
                        if (error instanceof RuntimeException) {
                            e = (RuntimeException) error;
                        } else {
                            e = new RuntimeException("Unexpected failure in OffsetsForLeaderEpoch " +
                                    "request for validating positions", error);
                        }
                        offsetFetcherUtils.onFailedResponseForValidatingPositions(fetchPositions, e);
                    }
                });
            });
    
            requestsToSend.addAll(unsentRequests);
        }
}
