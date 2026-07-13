public class kafka_0178 {

        void runOnce() {
            if (transactionManager != null) {
                try {
                    transactionManager.maybeResolveSequences();

                    RuntimeException lastError = transactionManager.lastError();
                    final boolean hasFatalError = transactionManager.hasFatalError();
                    final boolean hasAbortableAuthorizationError = transactionManager.hasAbortableError() && shouldHandleAuthorizationError(lastError);

                    if (hasFatalError) {
                        if (lastError != null)
                            maybeAbortBatches(lastError);
                        client.poll(retryBackoffMs, time.milliseconds());
                        return;
                    }

                    if (hasAbortableAuthorizationError) {
                        return;
                    }

                    transactionManager.bumpIdempotentEpochAndResetIdIfNeeded();

                    if (maybeSendAndPollTransactionalRequest()) {
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
}
