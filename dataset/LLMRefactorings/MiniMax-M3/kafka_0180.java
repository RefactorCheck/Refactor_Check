public class kafka_0180 {

        protected void maybeCloseFetchSessions(final Timer timer) {
            final List<RequestFuture<ClientResponse>> requestFutures = sendFetchesInternal(
                    prepareCloseFetchSessionRequests(),
                    this::handleCloseFetchSessionSuccess,
                    this::handleCloseFetchSessionFailure
            );
    
            while (timer.notExpired() && !allRequestsDone(requestFutures)) {
                client.poll(timer, null, true);
                timer.update();
            }
    
            if (!allRequestsDone(requestFutures)) {
                log.debug("All requests couldn't be sent in the specific timeout period {}ms. " +
                        "This may result in unnecessary fetch sessions at the broker. Consider increasing the timeout passed for " +
                        "KafkaConsumer.close(...)", timer.timeoutMs());
            }
        }
    
        private boolean allRequestsDone(final List<RequestFuture<ClientResponse>> requestFutures) {
            return requestFutures.stream().allMatch(RequestFuture::isDone);
        }
}
