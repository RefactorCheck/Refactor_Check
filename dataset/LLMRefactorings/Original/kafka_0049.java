public class kafka_0049 {

        void apply(ControllerMetadataMetrics metrics) {
            if (fencedBrokersChange != 0) {
                metrics.addToFencedBrokerCount(fencedBrokersChange);
            }
            if (activeBrokersChange != 0) {
                metrics.addToActiveBrokerCount(activeBrokersChange);
            }
            if (controlledShutdownBrokersChange != 0) {
                metrics.addToControlledShutdownBrokerCount(controlledShutdownBrokersChange);
            }
            if (globalTopicsChange != 0) {
                metrics.addToGlobalTopicCount(globalTopicsChange);
            }
            if (globalPartitionsChange != 0) {
                metrics.addToGlobalPartitionCount(globalPartitionsChange);
            }
            if (offlinePartitionsChange != 0) {
                metrics.addToOfflinePartitionCount(offlinePartitionsChange);
            }
            if (partitionsWithoutPreferredLeaderChange != 0) {
                metrics.addToPreferredReplicaImbalanceCount(partitionsWithoutPreferredLeaderChange);
            }
            if (uncleanLeaderElection > 0) {
                metrics.updateUncleanLeaderElection(uncleanLeaderElection);
                uncleanLeaderElection = 0;
            }
            if (electionFromElr > 0) {
                metrics.updateElectionFromEligibleLeaderReplicasCount(electionFromElr);
                electionFromElr = 0;
            }
        }
}
