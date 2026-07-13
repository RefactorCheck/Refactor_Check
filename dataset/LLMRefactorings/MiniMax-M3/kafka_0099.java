public class kafka_0099 {

    private void validateCooperativeAssignment(final Map<String, List<TopicPartition>> ownedPartitions,
                                               final Map<String, Assignment> assignments) {
        Set<TopicPartition> totalRevokedPartitions = new HashSet<>();
        SortedSet<TopicPartition> totalAddedPartitions = new TreeSet<>(COMPARATOR);
        for (final Map.Entry<String, Assignment> entry : assignments.entrySet()) {
            accumulatePartitionChanges(ownedPartitions.get(entry.getKey()),
                                       entry.getValue(),
                                       totalAddedPartitions,
                                       totalRevokedPartitions);
        }

        // if there are overlap between revoked partitions and added partitions, it means some partitions
        // immediately gets re-assigned to another member while it is still claimed by some member
        totalAddedPartitions.retainAll(totalRevokedPartitions);
        if (!totalAddedPartitions.isEmpty()) {
            log.error("With the COOPERATIVE protocol, owned partitions cannot be " +
                "reassigned to other members; however the assignor has reassigned partitions {} which are still owned " +
                "by some members", totalAddedPartitions);

            throw new IllegalStateException("Assignor supporting the COOPERATIVE protocol violates its requirements");
        }
    }

    private void accumulatePartitionChanges(final List<TopicPartition> ownedPartitionsForMember,
                                            final Assignment assignment,
                                            final SortedSet<TopicPartition> totalAddedPartitions,
                                            final Set<TopicPartition> totalRevokedPartitions) {
        final Set<TopicPartition> addedPartitions = new HashSet<>(assignment.partitions());
        addedPartitions.removeAll(ownedPartitionsForMember);
        final Set<TopicPartition> revokedPartitions = new HashSet<>(ownedPartitionsForMember);
        revokedPartitions.removeAll(assignment.partitions());

        totalAddedPartitions.addAll(addedPartitions);
        totalRevokedPartitions.addAll(revokedPartitions);
    }
}
