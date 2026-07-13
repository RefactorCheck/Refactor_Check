public class kafka_0295 {

        private PartitionLoadStats createPartitionLoadStatsForThisRackIfNeeded(int[] queueSizes, int[] partitionIds, String[] partitionLeaderRacks, int length) {
            if (!rackAware) {
                return null;
            }
            return buildPartitionLoadStatsForThisRack(queueSizes, partitionIds, partitionLeaderRacks, length);
        }

        private PartitionLoadStats buildPartitionLoadStatsForThisRack(int[] queueSizes, int[] partitionIds, String[] partitionLeaderRacks, int length) {
            int[] queueSizesInThisRack = new int[length];
            int[] partitionIdsInThisRack = new int[length];
            int lengthInThisRack = 0;
            int maxSizePlus1InThisRack = -1;

            for (int i = 0; i < length; i++) {
                if (rack.equals(partitionLeaderRacks[i])) {
                    queueSizesInThisRack[lengthInThisRack] = queueSizes[i];
                    partitionIdsInThisRack[lengthInThisRack] = partitionIds[i];

                    if (queueSizes[i] > maxSizePlus1InThisRack)
                        maxSizePlus1InThisRack = queueSizes[i];

                    lengthInThisRack += 1;
                }
            }
            ++maxSizePlus1InThisRack;

            invertAndFoldQueueSizeArray(queueSizesInThisRack, maxSizePlus1InThisRack, lengthInThisRack);
            return new PartitionLoadStats(queueSizesInThisRack, partitionIdsInThisRack, lengthInThisRack);
        }
}
