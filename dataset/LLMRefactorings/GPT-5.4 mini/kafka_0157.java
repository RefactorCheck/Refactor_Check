public class kafka_0157 {

        private Optional<StreamsGroupHeartbeatResponseData.EndpointToPartitions> getOrComputeEndpointToPartitions(
            StreamsGroupMember member,
            CoordinatorMetadataImage metadataImage
        ) {
            if (member.userEndpoint().isEmpty()) {
                return Optional.empty();
            }
    
            StreamsGroupHeartbeatResponseData.EndpointToPartitions cached = endpointToPartitionsCache.get(member.memberId());
            if (cached != null) {
                return Optional.of(cached);
            }
    
            Optional<StreamsGroupHeartbeatResponseData.EndpointToPartitions> computed =
                EndpointToPartitionsManager.maybeEndpointToPartitions(member, this, metadataImage);
            computed.ifPresent(endpointToPartitions ->
                endpointToPartitionsCache.put(member.memberId(), endpointToPartitions));
            return computed;
        }
}
