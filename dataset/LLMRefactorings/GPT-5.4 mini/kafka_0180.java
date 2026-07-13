public class kafka_0180 {

        protected void maybeCloseFetchSessions(final Timer timer) {
            final List<RequestFuture<ClientResponse>> fetchSessionFutures = sendFetchesInternal(
                    prepareCloseFetchSessionRequests(),
                    this::handleCloseFetchSessionSuccess,
                    this::handleCloseFetchSessionFailure
            );

            while (timer.notExpired() && !fetchSessionFutures.stream().allMatch(RequestFuture::isDone)) {
                client.poll(timer, null, true);
                timer.update();
            }

            if (!fetchSessionFutures.stream().allMatch(RequestFuture::isDone)) {
                log.debug("All requests couldn't be sent in the specific timeout period {}ms. " +
                        "This may result in unnecessary fetch sessions at the broker. Consider increasing the timeout passed for " +
                        "KafkaConsumer.close(...)", timer.timeoutMs());
            }
        }
}
