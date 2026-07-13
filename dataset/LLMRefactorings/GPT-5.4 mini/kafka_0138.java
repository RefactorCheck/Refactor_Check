public class kafka_0138 {

                private QuorumInfo createQuorumResult(final DescribeQuorumResponseData.PartitionData partition, DescribeQuorumResponseData.NodeCollection nodeCollection) {
                    List<QuorumInfo.ReplicaState> voters = partition.currentVoters().stream()
                        .map(this::translateReplicaState)
                        .collect(Collectors.toList());
    
                    List<QuorumInfo.ReplicaState> observers = partition.observers().stream()
                        .map(this::translateReplicaState)
                        .collect(Collectors.toList());
    
                    Map<Integer, QuorumInfo.Node> nodes = nodeCollection.stream().map(n ->
                        new QuorumInfo.Node(n.nodeId(), createEndpoints(n))
                    ).collect(Collectors.toMap(QuorumInfo.Node::nodeId, Function.identity()));
    
                    return new QuorumInfo(
                        partition.leaderId(),
                        partition.leaderEpoch(),
                        partition.highWatermark(),
                        voters,
                        observers,
                        nodes
                    );
                }

                private List<RaftVoterEndpoint> createEndpoints(final DescribeQuorumResponseData.Node n) {
                    return n.listeners().stream()
                        .map(l -> new RaftVoterEndpoint(l.name(), l.host(), l.port()))
                        .collect(Collectors.toList());
                }
}
