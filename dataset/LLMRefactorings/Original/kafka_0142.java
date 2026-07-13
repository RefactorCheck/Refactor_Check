public class kafka_0142 {

            @Override
            public Optional<AbstractRequest.Builder<?>> createRequest() {
                final ClientTelemetryState localState;
                final ClientTelemetrySubscription localSubscription;
    
                lock.readLock().lock();
                try {
                    localState = state;
                    localSubscription = subscription;
                } finally {
                    lock.readLock().unlock();
                }
    
                if (localState == ClientTelemetryState.SUBSCRIPTION_NEEDED) {
                    return createSubscriptionRequest(localSubscription);
                } else if (localState == ClientTelemetryState.PUSH_NEEDED || localState == ClientTelemetryState.TERMINATING_PUSH_NEEDED) {
                    return createPushRequest(localSubscription);
                }
    
                log.warn("Cannot make telemetry request as telemetry is in state: {}", localState);
                return Optional.empty();
            }
}
