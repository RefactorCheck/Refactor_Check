import java.util.function.IntConsumer;

public class kafka_0049 {

    void apply(ControllerMetadataMetrics metrics) {
        addIfNonZero(metrics, fencedBrokersChange, metrics::addToFencedBrokerCount);
        addIfNonZero(metrics, activeBrokersChange, metrics::addToActiveBrokerCount);
        addIfNonZero(metrics, controlledShutdownBrokersChange, metrics::addToControlledShutdownBrokerCount);
        addIfNonZero(metrics, globalTopicsChange, metrics::addToGlobalTopicCount);
        addIfNonZero(metrics, globalPartitionsChange, metrics::addToGlobalPartitionCount);
        addIfNonZero(metrics, offlinePartitionsChange, metrics::addToOfflinePartitionCount);
        addIfNonZero(metrics, partitionsWithoutPreferredLeaderChange, metrics::addToPreferredReplicaImbalanceCount);

        if (uncleanLeaderElection > 0) {
            metrics.updateUncleanLeaderElection(uncleanLeaderElection);
            uncleanLeaderElection = 0;
        }
        if (electionFromElr > 0) {
            metrics.updateElectionFromEligibleLeaderReplicasCount(electionFromElr);
            electionFromElr = 0;
        }
    }

    private void addIfNonZero(ControllerMetadataMetrics metrics, int change, IntConsumer adder) {
        if (change != 0) {
            adder.accept(change);
        }
    }
}
