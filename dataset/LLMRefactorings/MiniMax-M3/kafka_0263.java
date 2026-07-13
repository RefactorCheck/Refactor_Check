public class kafka_0263 {

        private void remove(int brokerId, Uuid topicId, int removedPartition, boolean leader) {
            if (leader) {
                removedPartition = removedPartition | LEADER_FLAG;
            }
            TimelineHashMap<Uuid, int[]> topicMap = isrMembers.get(brokerId);
            if (topicMap == null) {
                throw new RuntimeException("Broker " + brokerId + " has no isrMembers " +
                    "entry, so we can't remove " + topicId + ":" + removedPartition);
            }
            int[] partitions = topicMap.get(topicId);
            if (partitions == null) {
                throw new RuntimeException("Broker " + brokerId + " has no " +
                    "entry in isrMembers for topic " + topicId);
            }
            if (partitions.length == 1) {
                if (partitions[0] != removedPartition) {
                    throw new RuntimeException("Broker " + brokerId + " has no " +
                        "entry in isrMembers for " + topicId + ":" + removedPartition);
                }
                topicMap.remove(topicId);
                if (topicMap.isEmpty()) {
                    isrMembers.remove(brokerId);
                }
            } else {
                topicMap.put(topicId, removePartition(partitions, removedPartition));
            }
        }

        private int[] removePartition(int[] partitions, int removedPartition) {
            int[] newPartitions = new int[partitions.length - 1];
            int i = 0;
            for (int partition : partitions) {
                if (partition != removedPartition) {
                    newPartitions[i++] = partition;
                }
            }
            return newPartitions;
        }
}
