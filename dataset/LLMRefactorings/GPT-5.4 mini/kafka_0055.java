public class kafka_0055 {

        @Override
        public RenewDelegationTokenResult renewDelegationToken(final byte[] hmac, final RenewDelegationTokenOptions options) {
            final KafkaFutureImpl<Long> expiryTimeFuture = new KafkaFutureImpl<>();
            final long now = time.milliseconds();
            final long deadlineMs = calcDeadlineMs(now, options.timeoutMs());
            runnable.call(new Call("renewDelegationToken", deadlineMs,
                new LeastLoadedNodeProvider()) {

                @Override
                RenewDelegationTokenRequest.Builder createRequest(int timeoutMs) {
                    return new RenewDelegationTokenRequest.Builder(
                        new RenewDelegationTokenRequestData()
                            .setHmac(hmac)
                            .setRenewPeriodMs(options.renewTimePeriodMs()));
                }

                @Override
                void handleResponse(AbstractResponse abstractResponse) {
                    RenewDelegationTokenResponse response = (RenewDelegationTokenResponse) abstractResponse;
                    if (response.hasError()) {
                        expiryTimeFuture.completeExceptionally(response.error().exception());
                    } else {
                        expiryTimeFuture.complete(response.expiryTimestamp());
                    }
                }

                @Override
                void handleFailure(Throwable throwable) {
                    expiryTimeFuture.completeExceptionally(throwable);
                }
            }, now);

            return new RenewDelegationTokenResult(expiryTimeFuture);
        }
}
