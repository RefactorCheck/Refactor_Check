public class kafka_0138 {

                private QuorumInfo createQuorumResult(final DescribeQuorumResponseData.PartitionData partitionValue, DescribeQuorumResponseData.NodeCollection nodeCollection {
                    List<QuorumInfo.ReplicaState> voters = partitionValue.currentVoters().stream()
                        .map(this::translateReplicaState)
                        .collect(Collectors.toList());
    
                    List<QuorumInfo.ReplicaState> observers = partitionValue.observers().stream()
                        .map(this::translateReplicaState)
                        .collect(Collectors.toList());
    
                    Map<Integer, QuorumInfo.Node> nodes = nodeCollection.stream().map(n -> {
                        List<RaftVoterEndpoint> endpoints = n.listeners().stream()
                            .map(l -> new RaftVoterEndpoint(l.name(), l.host(), l.port()))
                            .collect(Collectors.toList());
    
                        return new QuorumInfo.Node(n.nodeId(), endpoints);
                    }).collect(Collectors.toMap(QuorumInfo.Node::nodeId, Function.identity()));
    
                    return new QuorumInfo(
                        partitionValue.leaderId(),
                        partitionValue.leaderEpoch(),
                        partitionValue.highWatermark(),
                        voters,
                        observers,
                        nodes
                    );
                }
}
