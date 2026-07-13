public class kafka_0273 {

        public boolean commitOffsetsSync(Map<TopicPartition, OffsetAndMetadata> offsets, Timer timer) {
            invokeCompletedOffsetCommitCallbacks();
    
            if (offsets.isEmpty()) {
                // We guarantee that the callbacks for all commitAsync() will be invoked when
                // commitSync() completes, even if the user tries to commit empty offsets.
                return invokePendingAsyncCommits(timer);
            }
    
            long attempts = 0L;
            do {
                if (coordinatorUnknownAndUnreadySync(timer)) {
                    return false;
                }
    
                RequestFuture<Void> future = sendOffsetCommitRequest(offsets);
                client.poll(future, timer);
    
                // We may have had in-flight offset commits when the synchronous commit began. If so, ensure that
                // the corresponding callbacks are invoked prior to returning in order to preserve the order that
                // the offset commits were applied.
                invokeCompletedOffsetCommitCallbacks();
    
                if (future.succeeded()) {
                    if (interceptors != null)
                        interceptors.onCommit(offsets);
                    return true;
                }
    
                if (future.failed() && !future.isRetriable())
                    throw future.exception();
    
                timer.sleep(retryBackoff.backoff(attempts++));
            } while (timer.notExpired());
    
            return false;
        }
}
