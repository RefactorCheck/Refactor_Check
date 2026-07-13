public class kafka_0221 {

        private void sendRequest(List<AlterPartitionItem> inflightAlterPartitionItems) {
            long brokerEpoch = brokerEpochSupplier.get();
            AlterPartitionRequest.Builder request = buildRequest(inflightAlterPartitionItems, brokerEpoch);
            log.debug("Sending AlterPartition to controller {}", request);
    
            // We will not time out AlterPartition request, instead letting it retry indefinitely
            // until a response is received, or a new LeaderAndIsr overwrites the existing isrState
            // which causes the response for those partitions to be ignored.
            controllerChannelManager.sendRequest(request,
                    new ControllerRequestCompletionHandler() {
                        @Override
                        public void onComplete(ClientResponse response) {
                            log.debug("Received AlterPartition response {}", response);
                            Errors error;
                            try {
                                if (response.authenticationException() != null) {
                                    // For now, we treat authentication errors as retriable. We use the
                                    // `NETWORK_EXCEPTION` error code for lack of a good alternative.
                                    // Note that `NodeToControllerChannelManager` will still log the
                                    // authentication errors so that users have a chance to fix the problem.
                                    error = Errors.NETWORK_EXCEPTION;
                                } else if (response.versionMismatch() != null) {
                                    error = Errors.UNSUPPORTED_VERSION;
                                } else {
                                    error = handleAlterPartitionResponse(
                                            (AlterPartitionResponse) response.responseBody(),
                                            brokerEpoch,
                                            inflightAlterPartitionItems
                                    );
                                }
                            } finally {
                                // clear the flag so future requests can proceed
                                clearInFlightRequest();
                            }
    
                            // check if we need to send another request right away
                            if (error == Errors.NONE) {
                                // In the normal case, check for pending updates to send immediately
                                maybePropagateIsrChanges();
                            } else {
                                // If we received a top-level error from the controller, retry the request in the near future
                                scheduler.scheduleOnce("send-alter-partition", () -> maybePropagateIsrChanges(), 50);
                            }
                        }
    
                        @Override
                        public void onTimeout() {
                            throw new IllegalStateException("Encountered unexpected timeout when sending AlterPartition to the controller");
                        }
                    });
        }
}
