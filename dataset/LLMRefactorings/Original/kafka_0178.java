public class kafka_0178 {

        void runOnce() {
            if (transactionManager != null) {
                try {
                    transactionManager.maybeResolveSequences();
    
                    RuntimeException lastError = transactionManager.lastError();
    
                    // do not continue sending if the transaction manager is in a failed state
                    if (transactionManager.hasFatalError()) {
                        if (lastError != null)
                            maybeAbortBatches(lastError);
                        client.poll(retryBackoffMs, time.milliseconds());
                        return;
                    }
    
                    if (transactionManager.hasAbortableError() && shouldHandleAuthorizationError(lastError)) {
                        return;
                    }
    
                    // Check whether we need a new producerId. If so, we will enqueue an InitProducerId
                    // request which will be sent below
                    transactionManager.bumpIdempotentEpochAndResetIdIfNeeded();
    
                    if (maybeSendAndPollTransactionalRequest()) {
                        return;
                    }
                } catch (AuthenticationException e) {
                    // This is already logged as error, but propagated here to perform any clean ups.
                    log.trace("Authentication exception while processing transactional request", e);
                    transactionManager.authenticationFailed(e);
                }
            }
    
            long currentTimeMs = time.milliseconds();
            long pollTimeout = sendProducerData(currentTimeMs);
            client.poll(pollTimeout, currentTimeMs);
        }
}
