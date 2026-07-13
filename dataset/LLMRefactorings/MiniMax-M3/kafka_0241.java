public class kafka_0241 {

    private void handleGroupError(
        CoordinatorKey groupId,
        Errors error,
        Map<CoordinatorKey, Throwable> failed,
        List<CoordinatorKey> groupsToUnmap
    ) {
        switch (error) {
            case GROUP_AUTHORIZATION_FAILED:
            case UNKNOWN_MEMBER_ID:
            case STALE_MEMBER_EPOCH:
                addToFailedMap(groupId, error, failed);
                break;
            case COORDINATOR_LOAD_IN_PROGRESS:
                logCoordinatorLoadingRetry(groupId);
                break;
            case COORDINATOR_NOT_AVAILABLE:
            case NOT_COORDINATOR:
                handleCoordinatorUnavailable(groupId, error, groupsToUnmap);
                break;
            default:
                handleUnexpectedError(groupId, error, failed);
        }
    }

    private void addToFailedMap(CoordinatorKey groupId, Errors error, Map<CoordinatorKey, Throwable> failed) {
        log.debug("`OffsetFetch` request for group id {} failed due to error {}", groupId.idValue, error);
        failed.put(groupId, error.exception());
    }

    private void logCoordinatorLoadingRetry(CoordinatorKey groupId) {
        log.debug("`OffsetFetch` request for group id {} failed because the coordinator " +
            "is still in the process of loading state. Will retry", groupId.idValue);
    }

    private void handleCoordinatorUnavailable(CoordinatorKey groupId, Errors error, List<CoordinatorKey> groupsToUnmap) {
        log.debug("`OffsetFetch` request for group id {} returned error {}. " +
            "Will attempt to find the coordinator again and retry", groupId.idValue, error);
        groupsToUnmap.add(groupId);
    }

    private void handleUnexpectedError(CoordinatorKey groupId, Errors error, Map<CoordinatorKey, Throwable> failed) {
        log.error("`OffsetFetch` request for group id {} failed due to unexpected error {}", groupId.idValue, error);
        failed.put(groupId, error.exception());
    }
}
