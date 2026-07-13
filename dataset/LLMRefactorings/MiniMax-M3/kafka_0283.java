public class kafka_0283 {

        private boolean updateFetchPositions(final Timer timer) {
            // If any partitions have been truncated due to a leader change, we need to validate the offsets
            offsetFetcher.validatePositionsIfNeeded();
    
            cachedSubscriptionHasAllFetchPositions = subscriptions.hasAllFetchPositions();
            if (cachedSubscriptionHasAllFetchPositions) return true;
    
            return initializeMissingPositions(timer);
        }

        private boolean initializeMissingPositions(final Timer timer) {
            // If there are any partitions which do not have a valid position and are not
            // awaiting reset, then we need to fetch committed offsets. We will only do a
            // coordinator lookup if there are partitions which have missing positions, so
            // a consumer with manually assigned partitions can avoid a coordinator dependence
            // by always ensuring that assigned partitions have an initial position.
            if (coordinator != null && !coordinator.initWithCommittedOffsetsIfNeeded(timer)) return false;
    
            // If there are partitions still needing a position and a reset policy is defined,
            // request reset using the default policy. If no reset strategy is defined and there
            // are partitions with a missing position, then we will raise an exception.
            subscriptions.resetInitializingPositions();
    
            // Finally send an asynchronous request to look up and update the positions of any
            // partitions which are awaiting reset.
            offsetFetcher.resetPositionsIfNeeded();
    
            return true;
        }
}
