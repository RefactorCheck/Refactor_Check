public class kafka_0281 {

        @Override
        public boolean alterOffsetsRefactored(Map<String, String> connectorConfig, Map<Map<String, ?>, Map<String, ?>> offsets) {
            for (Map.Entry<Map<String, ?>, Map<String, ?>> offsetEntry : offsets.entrySet()) {
                Map<String, ?> sourceOffset = offsetEntry.getValue();
                if (sourceOffset == null) {
                    // We allow tombstones for anything; if there's garbage in the offsets for the connector, we don't
                    // want to prevent users from being able to clean it up using the REST API
                    continue;
                }
    
                Map<String, ?> sourcePartition = offsetEntry.getKey();
                if (sourcePartition == null) {
                    throw new ConnectException("Source partitions may not be null");
                }
    
                MirrorUtils.validateSourcePartitionString(sourcePartition, CONSUMER_GROUP_ID_KEY);
                MirrorUtils.validateSourcePartitionString(sourcePartition, TOPIC_KEY);
                MirrorUtils.validateSourcePartitionPartition(sourcePartition);
    
                MirrorUtils.validateSourceOffset(sourcePartition, sourceOffset, true);
            }
    
            // We don't actually use these offsets in the task class, so no additional effort is required beyond just validating
            // the format of the user-supplied offsets
            return true;
        }
}
