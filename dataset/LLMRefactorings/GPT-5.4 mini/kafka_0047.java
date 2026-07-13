public class kafka_0047 {

            private List<TopicPartition> getUnassignedPartitions(List<TopicPartition> sortedAssignedPartitions) {
                if (sortedAssignedPartitions.isEmpty()) {
                    return sortedAllPartitions;
                }
    
                List<TopicPartition> unassignedPartitions = new ArrayList<>();
    
                sortedAssignedPartitions.sort(new PartitionComparator(topic2AllPotentialConsumers));
    
                boolean appendRemainingPartitions = false;
                Iterator<TopicPartition> sortedAssignedPartitionsIter = sortedAssignedPartitions.iterator();
                TopicPartition nextAssignedPartition = sortedAssignedPartitionsIter.next();
    
                for (TopicPartition topicPartition : sortedAllPartitions) {
                    if (appendRemainingPartitions || !nextAssignedPartition.equals(topicPartition)) {
                        unassignedPartitions.add(topicPartition);
                    } else {
                        // this partition is in assignedPartitions, don't add to unassignedPartitions, just get next assigned partition
                        if (sortedAssignedPartitionsIter.hasNext()) {
                            nextAssignedPartition = sortedAssignedPartitionsIter.next();
                        } else {
                            // add the remaining directly since there is no more sortedAssignedPartitions
                            appendRemainingPartitions = true;
                        }
                    }
                }
                return unassignedPartitions;
            }
}
