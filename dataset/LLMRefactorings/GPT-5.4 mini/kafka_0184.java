public class kafka_0184 {

        private void maybeUpdateLimitOffsetsForStandbyChangelogs(final Map<TaskId, Task> tasks) {
            if (shouldUpdateLimitOffsetsForStandbyChangelogs()) {
                final Set<TopicPartition> changelogsWithLimitOffsets = changelogs.entrySet().stream()
                    .filter(entry -> entry.getValue().stateManager.taskType() == Task.TaskType.STANDBY &&
                        entry.getValue().stateManager.changelogAsSource(entry.getKey()))
                    .map(Map.Entry::getKey).collect(Collectors.toSet());

                for (final TopicPartition partition : changelogsWithLimitOffsets) {
                    if (!changelogs.get(partition).bufferedRecords().isEmpty()) {
                        updateLimitOffsetsForStandbyChangelogs(committedOffsetForChangelogs(tasks, changelogsWithLimitOffsets));
                        lastUpdateOffsetTime = time.milliseconds();
                        break;
                    }
                }
            }
        }

        private boolean shouldUpdateLimitOffsetsForStandbyChangelogs() {
            return state == ChangelogReaderState.STANDBY_UPDATING &&
                updateOffsetIntervalMs < time.milliseconds() - lastUpdateOffsetTime;
        }
}
