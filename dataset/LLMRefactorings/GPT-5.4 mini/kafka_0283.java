public class kafka_0283 {

        private boolean fetchPositions(final Timer timer) {
            offsetFetcher.validatePositionsIfNeeded();

            cachedSubscriptionHasAllFetchPositions = subscriptions.hasAllFetchPositions();
            if (cachedSubscriptionHasAllFetchPositions) return true;

            if (coordinator != null && !coordinator.initWithCommittedOffsetsIfNeeded(timer)) return false;

            subscriptions.resetInitializingPositions();

            offsetFetcher.resetPositionsIfNeeded();

            return true;
        }
}
