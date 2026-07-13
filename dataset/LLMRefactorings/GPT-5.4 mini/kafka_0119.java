public class kafka_0119 {

        void handleBrokerChange(BrokerRegistration prev, BrokerRegistration next, ControllerMetadataMetrics metrics) {
            boolean previousFenced = false;
            boolean previousActive = false;
            boolean previousInControlledShutdown = false;
            if (prev != null) {
                previousFenced = prev.fenced();
                previousActive = !prev.fenced();
                previousInControlledShutdown = prev.inControlledShutdown();
            } else {
                metrics.addBrokerRegistrationStateMetric(next.id());
            }
            boolean nextFenced = false;
            boolean nextActive = false;
            boolean nextInControlledShutdown = false;
            final int brokerId;
            if (next != null) {
                nextFenced = next.fenced();
                nextActive = !next.fenced();
                nextInControlledShutdown = next.inControlledShutdown();
                brokerId = next.id();
            } else {
                brokerId = prev.id();
            }
            metrics.setBrokerRegistrationState(brokerId, next);
            fencedBrokersChange += delta(previousFenced, nextFenced);
            activeBrokersChange += delta(previousActive, nextActive);
            controlledShutdownBrokersChange += delta(previousInControlledShutdown, nextInControlledShutdown);
        }
}
