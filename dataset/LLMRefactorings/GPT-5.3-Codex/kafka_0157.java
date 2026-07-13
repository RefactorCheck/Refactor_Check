public class kafka_0157 {

        private Optional<StreamsGroupHeartbeatResponseData.EndpointToPartitions> getOrComputeEndpointToPartitionsRefactored(
            StreamsGroupMember member,
            CoordinatorMetadataImage metadataImage
        ) {
            if (member.userEndpoint().isEmpty()) {
                return Optional.empty();
            }
    
            String memberId = member.memberId();
    
            StreamsGroupHeartbeatResponseData.EndpointToPartitions cached = endpointToPartitionsCache.get(memberId);
            if (cached != null) {
                return Optional.of(cached);
            }
    
            Optional<StreamsGroupHeartbeatResponseData.EndpointToPartitions> computed =
                EndpointToPartitionsManager.maybeEndpointToPartitions(member, this, metadataImage);
            computed.ifPresent(endpointToPartitions ->
                endpointToPartitionsCache.put(memberId, endpointToPartitions));
            return computed;
        }
}
