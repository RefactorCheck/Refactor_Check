public class kafka_0251 {

    private static final long EARLIEST_OFFSET = 0L;

    protected Map<String, Map<Integer, LagInfo>> allLocalStorePartitionLags(final List<Task> tasksToCollectLagFor) {
        final Map<String, Map<Integer, LagInfo>> localStorePartitionLags = new TreeMap<>();
        final Collection<TopicPartition> allPartitions = new LinkedList<>();
        final Map<TopicPartition, Long> allChangelogPositions = new HashMap<>();

        // Obtain the current positions, of all the active-restoring and standby tasks
        for (final Task task : tasksToCollectLagFor) {
            allPartitions.addAll(task.changelogPartitions());
            // Note that not all changelog partitions, will have positions; since some may not have started
            allChangelogPositions.putAll(task.changelogOffsets());
        }

        log.debug("Current changelog positions: {}", allChangelogPositions);
        final Map<TopicPartition, ListOffsetsResultInfo> allEndOffsets;
        allEndOffsets = fetchEndOffsets(allPartitions, adminClient);
        log.debug("Current end offsets :{}", allEndOffsets);

        for (final Map.Entry<TopicPartition, ListOffsetsResultInfo> entry : allEndOffsets.entrySet()) {
            // Avoiding an extra admin API lookup by computing lags for not-yet-started restorations
            // from zero instead of the real "earliest offset" for the changelog.
            // This will yield the correct relative order of lagginess for the tasks in the cluster,
            // but it is an over-estimate of how much work remains to restore the task from scratch.
            final long changelogPosition = allChangelogPositions.getOrDefault(entry.getKey(), EARLIEST_OFFSET);
            final long latestOffset = entry.getValue().offset();
            final LagInfo lagInfo = new LagInfo(changelogPosition == Task.LATEST_OFFSET ? latestOffset : changelogPosition, latestOffset);
            final String storeName = streamsMetadataState.storeForChangelogTopic(entry.getKey().topic());
            localStorePartitionLags.computeIfAbsent(storeName, ignored -> new TreeMap<>())
                .put(entry.getKey().partition(), lagInfo);
        }

        return Collections.unmodifiableMap(localStorePartitionLags);
    }
}
