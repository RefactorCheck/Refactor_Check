public class kafka_0157 {

    private Optional<StreamsGroupHeartbeatResponseData.EndpointToPartitions> getOrComputeEndpointToPartitions(
        StreamsGroupMember member,
        CoordinatorMetadataImage metadataImage
    ) {
        if (member.userEndpoint().isEmpty()) {
            return Optional.empty();
        }

        String memberId = member.memberId();
        Optional<StreamsGroupHeartbeatResponseData.EndpointToPartitions> cached = getCachedEndpointToPartitions(memberId);
        if (cached.isPresent()) {
            return cached;
        }

        return computeAndCacheEndpointToPartitions(member, metadataImage, memberId);
    }

    private Optional<StreamsGroupHeartbeatResponseData.EndpointToPartitions> getCachedEndpointToPartitions(String memberId) {
        return Optional.ofNullable(endpointToPartitionsCache.get(memberId));
    }

    private Optional<StreamsGroupHeartbeatResponseData.EndpointToPartitions> computeAndCacheEndpointToPartitions(
        StreamsGroupMember member,
        CoordinatorMetadataImage metadataImage,
        String memberId
    ) {
        Optional<StreamsGroupHeartbeatResponseData.EndpointToPartitions> computed =
            EndpointToPartitionsManager.maybeEndpointToPartitions(member, this, metadataImage);
        computed.ifPresent(endpointToPartitions ->
            endpointToPartitionsCache.put(memberId, endpointToPartitions));
        return computed;
    }
}
