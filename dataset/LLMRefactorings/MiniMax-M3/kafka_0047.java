public class kafka_0047 {

    private List<TopicPartition> getUnassignedPartitions(List<TopicPartition> sortedAssignedPartitions) {
        if (sortedAssignedPartitions.isEmpty()) {
            return sortedAllPartitions;
        }

        List<TopicPartition> unassignedPartitions = new ArrayList<>();
        sortedAssignedPartitions.sort(new PartitionComparator(topic2AllPotentialConsumers));

        return collectUnassignedPartitions(sortedAssignedPartitions, unassignedPartitions);
    }

    private List<TopicPartition> collectUnassignedPartitions(List<TopicPartition> sortedAssignedPartitions,
                                                             List<TopicPartition> unassignedPartitions) {
        boolean shouldAddDirectly = false;
        Iterator<TopicPartition> iter = sortedAssignedPartitions.iterator();
        TopicPartition nextAssignedPartition = iter.next();

        for (TopicPartition topicPartition : sortedAllPartitions) {
            if (shouldAddDirectly || !nextAssignedPartition.equals(topicPartition)) {
                unassignedPartitions.add(topicPartition);
            } else {
                // this partition is in assignedPartitions, don't add to unassignedPartitions, just get next assigned partition
                if (iter.hasNext()) {
                    nextAssignedPartition = iter.next();
                } else {
                    // add the remaining directly since there is no more sortedAssignedPartitions
                    shouldAddDirectly = true;
                }
            }
        }
        return unassignedPartitions;
    }
}
