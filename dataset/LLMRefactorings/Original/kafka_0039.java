public class kafka_0039 {

        protected CompletedFetch initialize(final CompletedFetch completedFetch) {
            final TopicPartition tp = completedFetch.partition;
            final Errors error = Errors.forCode(completedFetch.partitionData.errorCode());
            boolean recordMetrics = true;
    
            try {
                if (!subscriptions.hasValidPosition(tp)) {
                    // this can happen when a rebalance happened while fetch is still in-flight
                    log.debug("Ignoring fetched records for partition {} since it no longer has valid position", tp);
                    return null;
                } else if (error == Errors.NONE) {
                    final CompletedFetch ret = handleInitializeSuccess(completedFetch);
                    recordMetrics = ret == null;
                    return ret;
                } else {
                    handleInitializeErrors(completedFetch, error);
                    return null;
                }
            } finally {
                if (recordMetrics) {
                    completedFetch.recordAggregatedMetrics(0, 0);
                }
    
                if (error != Errors.NONE)
                    // we move the partition to the end if there was an error. This way, it's more likely that partitions for
                    // the same topic can remain together (allowing for more efficient serialization).
                    subscriptions.movePartitionToEnd(tp);
            }
        }
}
