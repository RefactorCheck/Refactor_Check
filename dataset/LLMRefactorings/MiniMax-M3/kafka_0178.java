public class kafka_0178 {

    void runOnce() {
        if (transactionManager != null) {
            try {
                if (handleTransactions()) {
                    return;
                }
            } catch (AuthenticationException e) {
                log.trace("Authentication exception while processing transactional request", e);
                transactionManager.authenticationFailed(e);
            }
        }

        long currentTimeMs = time.milliseconds();
        long pollTimeout = sendProducerData(currentTimeMs);
        client.poll(pollTimeout, currentTimeMs);
    }

    private boolean handleTransactions() {
        transactionManager.maybeResolveSequences();

        RuntimeException lastError = transactionManager.lastError();

        if (transactionManager.hasFatalError()) {
            if (lastError != null)
                maybeAbortBatches(lastError);
            client.poll(retryBackoffMs, time.milliseconds());
            return true;
        }

        if (transactionManager.hasAbortableError() && shouldHandleAuthorizationError(lastError)) {
            return true;
        }

        transactionManager.bumpIdempotentEpochAndResetIdIfNeeded();

        return maybeSendAndPollTransactionalRequest();
    }
}
