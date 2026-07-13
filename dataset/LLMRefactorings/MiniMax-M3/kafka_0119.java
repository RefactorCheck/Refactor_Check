public class kafka_0119 {

        void handleBrokerChange(BrokerRegistration prev, BrokerRegistration next, ControllerMetadataMetrics metrics) {
            boolean wasFenced = false;
            boolean wasActive = false;
            boolean wasInControlledShutdown = false;
            if (prev != null) {
                RegistrationState state = captureState(prev);
                wasFenced = state.fenced;
                wasActive = state.active;
                wasInControlledShutdown = state.inControlledShutdown;
            } else {
                metrics.addBrokerRegistrationStateMetric(next.id());
            }
            boolean isFenced = false;
            boolean isActive = false;
            boolean isInControlledShutdown = false;
            final int brokerId;
            if (next != null) {
                RegistrationState state = captureState(next);
                isFenced = state.fenced;
                isActive = state.active;
                isInControlledShutdown = state.inControlledShutdown;
                brokerId = next.id();
            } else {
                brokerId = prev.id();
            }
            metrics.setBrokerRegistrationState(brokerId, next);
            fencedBrokersChange += delta(wasFenced, isFenced);
            activeBrokersChange += delta(wasActive, isActive);
            controlledShutdownBrokersChange += delta(wasInControlledShutdown, isInControlledShutdown);
        }

        private RegistrationState captureState(BrokerRegistration broker) {
            boolean fenced = broker.fenced();
            return new RegistrationState(fenced, !fenced, broker.inControlledShutdown());
        }

        private static class RegistrationState {
            final boolean fenced;
            final boolean active;
            final boolean inControlledShutdown;

            RegistrationState(boolean fenced, boolean active, boolean inControlledShutdown) {
                this.fenced = fenced;
                this.active = active;
                this.inControlledShutdown = inControlledShutdown;
            }
        }
}
