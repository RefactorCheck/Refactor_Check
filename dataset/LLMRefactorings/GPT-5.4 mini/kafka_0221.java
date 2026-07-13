public class kafka_0221 {

        private void sendRequest(List<AlterPartitionItem> inflightAlterPartitionItems) {
            long brokerEpoch = brokerEpochSupplier.get();
            AlterPartitionRequest.Builder request = buildRequest(inflightAlterPartitionItems, brokerEpoch);
            log.debug("Sending AlterPartition to controller {}", request);

            controllerChannelManager.sendRequest(request,
                    new ControllerRequestCompletionHandler() {
                        @Override
                        public void onComplete(ClientResponse response) {
                            log.debug("Received AlterPartition response {}", response);
                            Errors error;
                            try {
                                error = handleAlterPartitionCompletion(response, brokerEpoch, inflightAlterPartitionItems);
                            } finally {
                                clearInFlightRequest();
                            }
                            if (error == Errors.NONE) {
                                maybePropagateIsrChanges();
                            } else {
                                scheduler.scheduleOnce("send-alter-partition", () -> maybePropagateIsrChanges(), 50);
                            }
                        }

                        @Override
                        public void onTimeout() {
                            throw new IllegalStateException("Encountered unexpected timeout when sending AlterPartition to the controller");
                        }
                    });
        }

        private Errors handleAlterPartitionCompletion(ClientResponse response,
                                                      long brokerEpoch,
                                                      List<AlterPartitionItem> inflightAlterPartitionItems) {
            if (response.authenticationException() != null) {
                return Errors.NETWORK_EXCEPTION;
            }
            if (response.versionMismatch() != null) {
                return Errors.UNSUPPORTED_VERSION;
            }
            return handleAlterPartitionResponse(
                    (AlterPartitionResponse) response.responseBody(),
                    brokerEpoch,
                    inflightAlterPartitionItems
            );
        }
}
