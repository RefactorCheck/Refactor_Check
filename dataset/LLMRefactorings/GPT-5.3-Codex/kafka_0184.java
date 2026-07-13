public class kafka_0184 {

        private void maybeUpdateLimitOffsetsForStandbyChangelogsRefactored(final Map<TaskId, Task> tasks) {
            // we only consider updating the limit offset for standbys if we are not restoring active tasks
            if (state == ChangelogReaderState.STANDBY_UPDATING &&
                updateOffsetIntervalMs < time.milliseconds() - lastUpdateOffsetTime) {
    
                // when the interval has elapsed we should try to update the limit offset for standbys reading from
                // a source changelog with the new committed offset, unless there are no buffered records since 
                // we only need the limit when processing new records
                // for other changelog partitions we do not need to update limit offset at all since we never need to
                // check when it completes based on limit offset anyways: the end offset would keep increasing and the
                // standby never need to stop
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
}
