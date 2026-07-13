public class kafka_0252 {

            private void onFailure(final long currentTimeMs,
                                   final Errors responseError) {
                log.debug("Offset fetch failed: {}", responseError.message());
                onFailedAttempt(currentTimeMs);
                ApiException exception = responseError.exception();
                final boolean coordinatorUnavailable = responseError == Errors.NOT_COORDINATOR || responseError == Errors.COORDINATOR_NOT_AVAILABLE;
                if (responseError == COORDINATOR_LOAD_IN_PROGRESS) {
                    future.completeExceptionally(exception);
                } else if (responseError == Errors.UNKNOWN_MEMBER_ID) {
                    log.error("OffsetFetch failed with {} because the member is not part of the group" +
                        " anymore.", responseError);
                    future.completeExceptionally(exception);
                } else if (responseError == Errors.STALE_MEMBER_EPOCH) {
                    log.error("OffsetFetch failed with {} and the consumer is not part " +
                        "of the group anymore (it probably left the group, got fenced" +
                        " or failed). The request cannot be retried and will fail.", responseError);
                    future.completeExceptionally(exception);
                } else if (coordinatorUnavailable) {
                    coordinatorRequestManager.markCoordinatorUnknown("error response " + responseError.name(), currentTimeMs);
                    future.completeExceptionally(exception);
                } else if (exception instanceof RetriableException) {
                    future.completeExceptionally(exception);
                } else if (responseError == Errors.GROUP_AUTHORIZATION_FAILED) {
                    future.completeExceptionally(GroupAuthorizationException.forGroupId(groupId));
                } else {
                    future.completeExceptionally(new KafkaException("Unexpected error in fetch offset response: " + responseError.message()));
                }
            }
}
