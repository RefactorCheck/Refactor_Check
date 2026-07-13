public class kafka_0281 {

        @Override
        public boolean alterOffsets(Map<String, String> connectorConfig, Map<Map<String, ?>, Map<String, ?>> offsets) {
            for (Map.Entry<Map<String, ?>, Map<String, ?>> offsetEntry : offsets.entrySet()) {
                validateOffsetEntry(offsetEntry);
            }
            return true;
        }

        private void validateOffsetEntry(Map.Entry<Map<String, ?>, Map<String, ?>> offsetEntry) {
            Map<String, ?> sourceOffset = offsetEntry.getValue();
            if (sourceOffset == null) {
                return;
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
}
