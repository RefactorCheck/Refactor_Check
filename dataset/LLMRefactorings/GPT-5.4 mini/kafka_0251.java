public class kafka_0251 {

        protected Map<String, Map<Integer, LagInfo>> allLocalStorePartitionLags(final List<Task> tasksToCollectLagFor) {
            final Map<String, Map<Integer, LagInfo>> localStorePartitionLags = new TreeMap<>();
            final Collection<TopicPartition> allPartitions = new LinkedList<>();
            final Map<TopicPartition, Long> allChangelogPositions = new HashMap<>();

            for (final Task task : tasksToCollectLagFor) {
                allPartitions.addAll(task.changelogPartitions());
                allChangelogPositions.putAll(task.changelogOffsets());
            }

            log.debug("Current changelog positions: {}", allChangelogPositions);
            final Map<TopicPartition, ListOffsetsResultInfo> allEndOffsets = fetchEndOffsets(allPartitions, adminClient);
            log.debug("Current end offsets :{}", allEndOffsets);

            for (final Map.Entry<TopicPartition, ListOffsetsResultInfo> entry : allEndOffsets.entrySet()) {
                final String storeName = streamsMetadataState.storeForChangelogTopic(entry.getKey().topic());
                final Map<Integer, LagInfo> partitionLags =
                    localStorePartitionLags.computeIfAbsent(storeName, ignored -> new TreeMap<>());
                partitionLags.put(entry.getKey().partition(), buildLagInfo(entry, allChangelogPositions));
            }

            return Collections.unmodifiableMap(localStorePartitionLags);
        }

        private LagInfo buildLagInfo(final Map.Entry<TopicPartition, ListOffsetsResultInfo> entry,
                                     final Map<TopicPartition, Long> allChangelogPositions) {
            final long earliestOffset = 0L;
            final long changelogPosition = allChangelogPositions.getOrDefault(entry.getKey(), earliestOffset);
            final long latestOffset = entry.getValue().offset();
            final long currentOffset = changelogPosition == Task.LATEST_OFFSET ? latestOffset : changelogPosition;
            return new LagInfo(currentOffset, latestOffset);
        }
}
