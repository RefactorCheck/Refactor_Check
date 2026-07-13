public class kafka_0252 {

            private void onFailure(final long currentTimeMs,
                                   final Errors responseError) {
                log.debug("Offset fetch failed: {}", responseError.message());
                onFailedAttempt(currentTimeMs);
                future.completeExceptionally(determineException(responseError, currentTimeMs));
            }

            private Throwable determineException(final Errors responseError, final long currentTimeMs) {
                if (responseError == COORDINATOR_LOAD_IN_PROGRESS) {
                    return responseError.exception();
                } else if (responseError == Errors.UNKNOWN_MEMBER_ID) {
                    log.error("OffsetFetch failed with {} because the member is not part of the group" +
                        " anymore.", responseError);
                    return responseError.exception();
                } else if (responseError == Errors.STALE_MEMBER_EPOCH) {
                    log.error("OffsetFetch failed with {} and the consumer is not part " +
                        "of the group anymore (it probably left the group, got fenced" +
                        " or failed). The request cannot be retried and will fail.", responseError);
                    return responseError.exception();
                } else if (responseError == Errors.NOT_COORDINATOR || responseError == Errors.COORDINATOR_NOT_AVAILABLE) {
                    // Re-discover the coordinator and retry
                    coordinatorRequestManager.markCoordinatorUnknown("error response " + responseError.name(), currentTimeMs);
                    return responseError.exception();
                } else if (responseError.exception() instanceof RetriableException) {
                    return responseError.exception();
                } else if (responseError == Errors.GROUP_AUTHORIZATION_FAILED) {
                    return GroupAuthorizationException.forGroupId(groupId);
                } else {
                    // Fail with a non-retriable KafkaException for all unexpected errors
                    return new KafkaException("Unexpected error in fetch offset response: " + responseError.message());
                }
            }
}
