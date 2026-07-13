public class kafka_0271 {

        public boolean cleanupExpiredOffsets(String groupId, List<CoordinatorRecord> records) {
            TimelineHashMap<String, TimelineHashMap<Integer, OffsetAndMetadata>> offsetsByTopic =
                offsets.offsetsByGroup.get(groupId);
            final boolean groupHasOpenTransactions = openTransactions.contains(groupId);
            if (offsetsByTopic == null) {
                return !groupHasOpenTransactions;
            }

            // We expect the group to exist.
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
                    partitions.forEach((partition, offsetAndMetadata) -> {
                        if (condition.isOffsetExpired(offsetAndMetadata, currentTimestampMs, config.offsetsRetentionMs())
                            && !hasPendingTransactionalOffsets(groupId, topic, partition)) {
                            appendOffsetCommitTombstone(groupId, topic, partition, records);
                            log.debug("[GroupId {}] Expired offset for partition={}-{}", groupId, topic, partition);
                        } else {
                            allOffsetsExpired.set(false);
                        }
                    });
                } else {
                    allOffsetsExpired.set(false);
                }
            });
            metrics.record(OFFSET_EXPIRED_SENSOR_NAME, records.size());

            // We don't want to remove the group if there are ongoing transactions with undeleted offsets.
            return allOffsetsExpired.get() && !groupHasOpenTransactions;
        }
}
