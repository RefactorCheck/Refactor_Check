public class kafka_0099 {

        private void validateCooperativeAssignmentRefactored(final Map<String, List<TopicPartition>> ownedPartitions,
                                                   final Map<String, Assignment> assignments) {
            Set<TopicPartition> totalRevokedPartitions = new HashSet<>();
            SortedSet<TopicPartition> totalAddedPartitions = new TreeSet<>(COMPARATOR);
            for (final Map.Entry<String, Assignment> entry : assignments.entrySet()) {
                final Assignment assignment = entry.getValue();
                final Set<TopicPartition> addedPartitions = new HashSet<>(assignment.partitions());
                addedPartitions.removeAll(ownedPartitions.get(entry.getKey()));
                final Set<TopicPartition> revokedPartitions = new HashSet<>(ownedPartitions.get(entry.getKey()));
                revokedPartitions.removeAll(assignment.partitions());
    
                totalAddedPartitions.addAll(addedPartitions);
                totalRevokedPartitions.addAll(revokedPartitions);
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
}
