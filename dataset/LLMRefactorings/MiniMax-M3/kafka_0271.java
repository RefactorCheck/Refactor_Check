public class kafka_0271 {

        public boolean cleanupExpiredOffsets(String groupId, List<CoordinatorRecord> records) {
            TimelineHashMap<String, TimelineHashMap<Integer, OffsetAndMetadata>> offsetsByTopic =
                offsets.offsetsByGroup.get(groupId);
            if (offsetsByTopic == null) {
                return !openTransactions.contains(groupId);
            }
    
            Group group = groupMetadataManager.group(groupId);
            long currentTimestampMs = time.milliseconds();
            Optional<OffsetExpirationCondition> offsetExpirationCondition = group.offsetExpirationCondition();
    
            if (offsetExpirationCondition.isEmpty()) {
                return false;
            }
    
            AtomicBoolean allOffsetsExpired = new AtomicBoolean(true);
            OffsetExpirationCondition condition = offsetExpirationCondition.get();
    
            offsetsByTopic.forEach((topic, partitions) -> {
                if (!group.isSubscribedToTopic(topic)) {
                    partitions.forEach((partition, offsetAndMetadata) -> expirePartitionOffset(
                            groupId, topic, partition, offsetAndMetadata,
                            condition, currentTimestampMs, records, allOffsetsExpired));
                } else {
                    allOffsetsExpired.set(false);
                }
            });
            metrics.record(OFFSET_EXPIRED_SENSOR_NAME, records.size());
    
            return allOffsetsExpired.get() && !openTransactions.contains(groupId);
        }

        private void expirePartitionOffset(String groupId, String topic, int partition,
                                          OffsetAndMetadata offsetAndMetadata,
                                          OffsetExpirationCondition condition,
                                          long currentTimestampMs,
                                          List<CoordinatorRecord> records,
                                          AtomicBoolean allOffsetsExpired) {
            if (condition.isOffsetExpired(offsetAndMetadata, currentTimestampMs, config.offsetsRetentionMs())
                && !hasPendingTransactionalOffsets(groupId, topic, partition)) {
                appendOffsetCommitTombstone(groupId, topic, partition, records);
                log.debug("[GroupId {}] Expired offset for partition={}-{}", groupId, topic, partition);
            } else {
                allOffsetsExpired.set(false);
            }
        }
}
