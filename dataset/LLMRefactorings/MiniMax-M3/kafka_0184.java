public class kafka_0184 {

        private void maybeUpdateLimitOffsetsForStandbyChangelogs(final Map<TaskId, Task> tasks) {
            if (state == ChangelogReaderState.STANDBY_UPDATING &&
                updateOffsetIntervalMs < time.milliseconds() - lastUpdateOffsetTime) {
    
                final Set<TopicPartition> changelogsWithLimitOffsets = changelogsWithLimitOffsets();
    
                for (final TopicPartition partition : changelogsWithLimitOffsets) {
                    if (!changelogs.get(partition).bufferedRecords().isEmpty()) {
                        updateLimitOffsetsForStandbyChangelogs(committedOffsetForChangelogs(tasks, changelogsWithLimitOffsets));
                        lastUpdateOffsetTime = time.milliseconds();
                        break;
                    }
                }
            }
        }

        private Set<TopicPartition> changelogsWithLimitOffsets() {
            return changelogs.entrySet().stream()
                .filter(entry -> entry.getValue().stateManager.taskType() == Task.TaskType.STANDBY &&
                    entry.getValue().stateManager.changelogAsSource(entry.getKey()))
                .map(Map.Entry::getKey).collect(Collectors.toSet());
        }
}
