public class kafka_0121 {

        private CompletableFuture<OffsetsForLeaderEpochUtils.OffsetForEpochResult> buildOffsetsForLeaderEpochRequestToNode(
                final Node node,
                final Map<TopicPartition, SubscriptionState.FetchPosition> fetchPositions,
                List<NetworkClientDelegate.UnsentRequest> unsentRequests) {
            AbstractRequest.Builder<OffsetsForLeaderEpochRequest> builder =
                    OffsetsForLeaderEpochUtils.prepareRequest(fetchPositions);
    
            log.debug("Creating OffsetsForLeaderEpoch request request {} to broker {}", builder, node);
    
            NetworkClientDelegate.UnsentRequest unsentRequest = new NetworkClientDelegate.UnsentRequest(
                    builder,
                    Optional.ofNullable(node));
            unsentRequests.add(unsentRequest);
            CompletableFuture<OffsetsForLeaderEpochUtils.OffsetForEpochResult> result = new CompletableFuture<>();
            unsentRequest.whenComplete((response, error) -> handleOffsetsForLeaderEpochResponse(
                    node, fetchPositions, builder, response, error, result));
            return result;
        }

        private void handleOffsetsForLeaderEpochResponse(
                Node node,
                Map<TopicPartition, SubscriptionState.FetchPosition> fetchPositions,
                AbstractRequest.Builder<OffsetsForLeaderEpochRequest> builder,
                ClientResponse response,
                Throwable error,
                CompletableFuture<OffsetsForLeaderEpochUtils.OffsetForEpochResult> result) {
            if (error != null) {
                log.debug("Sending OffsetsForLeaderEpoch request {} to broker {} failed",
                        builder,
                        node,
                        error);
                result.completeExceptionally(error);
            } else {
                OffsetsForLeaderEpochResponse offsetsForLeaderEpochResponse = (OffsetsForLeaderEpochResponse) response.responseBody();
                log.trace("Received OffsetsForLeaderEpoch response {} from broker {}", offsetsForLeaderEpochResponse, node);
                try {
                    OffsetsForLeaderEpochUtils.OffsetForEpochResult listOffsetResult =
                            OffsetsForLeaderEpochUtils.handleResponse(fetchPositions, offsetsForLeaderEpochResponse);
                    result.complete(listOffsetResult);
                } catch (RuntimeException e) {
                    result.completeExceptionally(e);
                }
            }
        }
}
