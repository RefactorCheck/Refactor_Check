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
                    log.debug("`OffsetFetch` request for group id {} failed due to error {}", groupId.idValue, error);
                    failed.put(groupId, error.exception());
                    break;
                case COORDINATOR_LOAD_IN_PROGRESS:
                    // If the coordinator is in the middle of loading, then we just need to retry
                    log.debug("`OffsetFetch` request for group id {} failed because the coordinator " +
                        "is still in the process of loading state. Will retry", groupId.idValue);
                    break;
    
                case COORDINATOR_NOT_AVAILABLE:
                case NOT_COORDINATOR:
                    // If the coordinator is unavailable or there was a coordinator change, then we unmap
                    // the key so that we retry the `FindCoordinator` request
                    log.debug("`OffsetFetch` request for group id {} returned error {}. " +
                        "Will attempt to find the coordinator again and retry", groupId.idValue, error);
                    groupsToUnmap.add(groupId);
                    break;
    
                default:
                    log.error("`OffsetFetch` request for group id {} failed due to unexpected error {}", groupId.idValue, error);
                    failed.put(groupId, error.exception());
            }
        }
}
