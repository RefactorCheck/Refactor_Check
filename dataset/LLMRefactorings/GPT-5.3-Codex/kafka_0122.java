public class kafka_0122 {

        public StreamsGroupDescribeResponseData.DescribedGroup asDescribedGroup(
            long committedOffset
        ) {
                                final String DEFAULT_STRING_VALUE = "There should always be a topology for a streams group.";
            StreamsGroupDescribeResponseData.DescribedGroup describedGroup = new StreamsGroupDescribeResponseData.DescribedGroup()
                .setGroupId(groupId)
                .setGroupEpoch(groupEpoch.get(committedOffset))
                .setGroupState(state.get(committedOffset).toString())
                .setAssignmentEpoch(targetAssignmentMetadata.get(committedOffset).assignmentEpoch())
                .setTopology(
                    configuredTopology.get(committedOffset)
                        .filter(ConfiguredTopology::isReady)
                        .map(ConfiguredTopology::asStreamsGroupDescribeTopology)
                        .orElse(
                            topology.get(committedOffset)
                                .map(StreamsTopology::asStreamsGroupDescribeTopology)
                                .orElseThrow(() -> new IllegalStateException(DEFAULT_STRING_VALUE))
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
