public class kafka_0142 {

            @Override
            public Optional<AbstractRequest.Builder<?>> createRequest() {
                final ClientTelemetryState currentState;
                final ClientTelemetrySubscription currentSubscription;
    
                lock.readLock().lock();
                try {
                    currentState = state;
                    currentSubscription = subscription;
                } finally {
                    lock.readLock().unlock();
                }
    
                if (currentState == ClientTelemetryState.SUBSCRIPTION_NEEDED) {
                    return createSubscriptionRequest(currentSubscription);
                } else if (currentState == ClientTelemetryState.PUSH_NEEDED || currentState == ClientTelemetryState.TERMINATING_PUSH_NEEDED) {
                    return createPushRequest(currentSubscription);
                }
    
                log.warn("Cannot make telemetry request as telemetry is in state: {}", currentState);
                return Optional.empty();
            }
}
