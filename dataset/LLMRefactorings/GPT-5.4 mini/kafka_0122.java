public class kafka_0122 {

        public StreamsGroupDescribeResponseData.DescribedGroup asDescribedGroup(
            long committedOffset
        ) {
            final Optional<ConfiguredTopology> configuredTopologyAtOffset = configuredTopology.get(committedOffset);
            final Optional<StreamsTopology> topologyAtOffset = topology.get(committedOffset);
            StreamsGroupDescribeResponseData.DescribedGroup describedGroup = new StreamsGroupDescribeResponseData.DescribedGroup()
                .setGroupId(groupId)
                .setGroupEpoch(groupEpoch.get(committedOffset))
                .setGroupState(state.get(committedOffset).toString())
                .setAssignmentEpoch(targetAssignmentMetadata.get(committedOffset).assignmentEpoch())
                .setTopology(
                    configuredTopologyAtOffset
                        .filter(ConfiguredTopology::isReady)
                        .map(ConfiguredTopology::asStreamsGroupDescribeTopology)
                        .orElse(
                            topologyAtOffset
                                .map(StreamsTopology::asStreamsGroupDescribeTopology)
                                .orElseThrow(() -> new IllegalStateException("There should always be a topology for a streams group."))
                        )
                );
            members.entrySet(committedOffset).forEach(
                entry -> describedGroup.members().add(
                    entry.getValue().asStreamsGroupDescribeMember(
                        targetAssignment.get(entry.getValue().memberId(), committedOffset)
                    )
                )
            );
            return describedGroup;
        }
}
