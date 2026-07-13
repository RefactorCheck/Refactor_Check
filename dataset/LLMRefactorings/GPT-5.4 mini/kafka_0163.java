public class kafka_0163 {

        private void onFailure(final Throwable exception, final long responseTimeMs) {
            heartbeatRequestState.onFailedAttempt(responseTimeMs);
            heartbeatState.reset();
            if (exception instanceof RetriableException) {
                coordinatorRequestManager.handleCoordinatorDisconnect(exception, responseTimeMs);
                logger.debug(String.format("StreamsGroupHeartbeatRequest failed because of a retriable exception. Will retry in %s ms: %s",
                    heartbeatRequestState.remainingBackoffMs(responseTimeMs),
                    exception.getMessage()));
                membershipManager.onRetriableHeartbeatFailure();
            } else {
                if (exception instanceof UnsupportedVersionException) {
                    logger.error("StreamsGroupHeartbeatRequest failed because of an unsupported version exception: {}",
                        exception.getMessage());
                    handleFatalFailure(new UnsupportedVersionException(UNSUPPORTED_VERSION_ERROR_MESSAGE));
                } else {
                    logger.error("StreamsGroupHeartbeatRequest failed because of a fatal exception while sending request: {}",
                        exception.getMessage());
                    handleFatalFailure(exception);
                }
                membershipManager.onFatalHeartbeatFailure();
            }
        }
}
