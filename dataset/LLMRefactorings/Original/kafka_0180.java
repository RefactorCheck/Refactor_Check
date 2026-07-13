public class kafka_0180 {

        protected void maybeCloseFetchSessions(final Timer timer) {
            final List<RequestFuture<ClientResponse>> requestFutures = sendFetchesInternal(
                    prepareCloseFetchSessionRequests(),
                    this::handleCloseFetchSessionSuccess,
                    this::handleCloseFetchSessionFailure
            );
    
            // Poll to ensure that request has been written to the socket. Wait until either the timer has expired or until
            // all requests have received a response.
            while (timer.notExpired() && !requestFutures.stream().allMatch(RequestFuture::isDone)) {
                client.poll(timer, null, true);
                timer.update();
            }
    
            if (!requestFutures.stream().allMatch(RequestFuture::isDone)) {
                // we ran out of time before completing all futures. It is ok since we don't want to block the shutdown
                // here.
                log.debug("All requests couldn't be sent in the specific timeout period {}ms. " +
                        "This may result in unnecessary fetch sessions at the broker. Consider increasing the timeout passed for " +
                        "KafkaConsumer.close(...)", timer.timeoutMs());
            }
        }
}
