public class kafka_0295 {

        private PartitionLoadStats createPartitionLoadStatsForThisRackIfNeeded(int[] queueSizes, int[] partitionIds, String[] partitionLeaderRacks, int length) {
            if (!rackAware) {
                return null;
            }
            int[] queueSizesInThisRack = new int[length];
            int[] partitionIdsInThisRack = new int[length];
            int rackPartitionCount = 0;
            int maxQueueSizePlusOneInThisRack = -1;
    
            for (int i = 0; i < length; i++) {
                if (rack.equals(partitionLeaderRacks[i])) {
                    queueSizesInThisRack[rackPartitionCount] = queueSizes[i];
                    partitionIdsInThisRack[rackPartitionCount] = partitionIds[i];
    
                    if (queueSizes[i] > maxQueueSizePlusOneInThisRack)
                        maxQueueSizePlusOneInThisRack = queueSizes[i];
    
                    rackPartitionCount += 1;
                }
            }
            ++maxQueueSizePlusOneInThisRack;
    
            invertAndFoldQueueSizeArray(queueSizesInThisRack, maxQueueSizePlusOneInThisRack, rackPartitionCount);
            return new PartitionLoadStats(queueSizesInThisRack, partitionIdsInThisRack, rackPartitionCount);
        }
}
