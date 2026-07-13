public class kafka_0165 {

        @Override
        public boolean alterOffsets(Map<String, String> config, Map<Map<String, ?>, Map<String, ?>> offsets) {
            for (Map.Entry<Map<String, ?>, Map<String, ?>> offsetEntry : offsets.entrySet()) {
                Map<String, ?> sourceOffset = offsetEntry.getValue();
                if (sourceOffset == null) {
                    continue;
                }
                validateOffsetEntry(offsetEntry);
            }
            return true;
        }

        private void validateOffsetEntry(Map.Entry<Map<String, ?>, Map<String, ?>> offsetEntry) {
            Map<String, ?> sourcePartition = offsetEntry.getKey();
            if (sourcePartition == null) {
                throw new ConnectException("Source partitions may not be null");
            }
            Map<String, ?> sourceOffset = offsetEntry.getValue();
            MirrorUtils.validateSourcePartitionString(sourcePartition, SOURCE_CLUSTER_ALIAS_KEY);
            MirrorUtils.validateSourcePartitionString(sourcePartition, TARGET_CLUSTER_ALIAS_KEY);
            MirrorUtils.validateSourceOffset(sourcePartition, sourceOffset, true);
        }
}
