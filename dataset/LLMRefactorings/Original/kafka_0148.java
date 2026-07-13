public class kafka_0148 {

        @Override
        public CompletableFuture<BrokerHeartbeatReply> processBrokerHeartbeat(
            ControllerRequestContext context,
            BrokerHeartbeatRequestData request
        ) {
            controllerMetrics.updateBrokerContactTime(request.brokerId());
            // We start by updating the broker heartbeat in a lockless data structure.
            // We do this first so that if the main controller thread is backlogged, the
            // last contact time update still gets through.
            if (!clusterControl.trackBrokerHeartbeat(request.brokerId(), request.brokerEpoch())) {
                // Normally, ControllerWriteOperation would automatically check if the controller is
                // active. But since we're doing this outside of the main controller thread, we have to
                // do our own check here, and handle the case where we are inactive.
                throw ControllerExceptions.newWrongControllerException(latestController());
            }
            // The next part takes place in the main controller thread and may involve generating
            // metadata records.
            return appendWriteEvent("processBrokerHeartbeat", context.deadlineNs(),
                new ControllerWriteOperation<BrokerHeartbeatReply>() {
                    private final int brokerId = request.brokerId();
                    private boolean inControlledShutdown = false;
    
                    @Override
                    public ControllerResult<BrokerHeartbeatReply> generateRecordsAndResult() {
                        // Get the offset of the broker registration. Note: although the offset
                        // we get back here could be the offset for a previous epoch of the
                        // broker registration, we will check the broker epoch in
                        // processBrokerHeartbeat, which covers that case.
                        OptionalLong offsetForRegisterBrokerRecord =
                                clusterControl.registerBrokerRecordOffset(brokerId);
                        if (offsetForRegisterBrokerRecord.isEmpty()) {
                            throw new StaleBrokerEpochException(
                                String.format("Receive a heartbeat from broker %d before registration", brokerId));
                        }
                        ControllerResult<BrokerHeartbeatReply> result = replicationControl.
                            processBrokerHeartbeat(request, offsetForRegisterBrokerRecord.getAsLong());
                        inControlledShutdown = result.response().inControlledShutdown();
                        return result;
                    }
    
                    @Override
                    public void processBatchEndOffset(long offset) {
                        if (inControlledShutdown) {
                            clusterControl.heartbeatManager().
                                maybeUpdateControlledShutdownOffset(brokerId, offset);
                        }
                    }
                },
                EnumSet.noneOf(ControllerOperationFlag.class)).whenComplete((__, t) -> {
                    if (ControllerExceptions.isTimeoutException(t)) {
                        replicationControl.processExpiredBrokerHeartbeat(request);
                        controllerMetrics.incrementTimedOutHeartbeats();
                    }
                });
        }
}
