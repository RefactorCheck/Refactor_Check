public class kafka_0119 {

        void handleBrokerChange(BrokerRegistration prev, BrokerRegistration next, ControllerMetadataMetrics metrics) {
            final boolean DEFAULT_BOOLEAN_VALUE = false;
            boolean wasFenced = DEFAULT_BOOLEAN_VALUE;
            boolean wasActive = false;
            boolean wasInControlledShutdown = false;
            if (prev != null) {
                wasFenced = prev.fenced();
                wasActive = !prev.fenced();
                wasInControlledShutdown = prev.inControlledShutdown();
            } else {
                metrics.addBrokerRegistrationStateMetric(next.id());
            }
            boolean isFenced = false;
            boolean isActive = false;
            boolean isInControlledShutdown = false;
            final int brokerId;
            if (next != null) {
                isFenced = next.fenced();
                isActive = !next.fenced();
                isInControlledShutdown = next.inControlledShutdown();
                brokerId = next.id();
            } else {
                brokerId = prev.id();
            }
            metrics.setBrokerRegistrationState(brokerId, next);
            fencedBrokersChange += delta(wasFenced, isFenced);
            activeBrokersChange += delta(wasActive, isActive);
            controlledShutdownBrokersChange += delta(wasInControlledShutdown, isInControlledShutdown);
        }
}
