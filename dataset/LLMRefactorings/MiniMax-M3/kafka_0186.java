public class kafka_0186 {

    public Map<TaskId, Long> taskOffsetSums() {
        final Map<TaskId, Task> tasks = allTasks();
        final Set<TaskId> lockedTaskDirectoriesOfNonOwnedTasksAndClosedAndCreatedTasks =
            union(HashSet::new, lockedTaskDirectories, tasks.keySet());

        final Map<TaskId, Long> taskOffsetSums = stateDirectory.taskOffsetSums(lockedTaskDirectoriesOfNonOwnedTasksAndClosedAndCreatedTasks);

        overlayLatestOffsetsFromAssignedTasks(taskOffsetSums, tasks);

        return taskOffsetSums;
    }

    private void overlayLatestOffsetsFromAssignedTasks(final Map<TaskId, Long> taskOffsetSums, final Map<TaskId, Task> tasks) {
        for (final Task task : tasks.values()) {
            if (task.isActive() && task.state() == State.RUNNING && !task.changelogPartitions().isEmpty()) {
                taskOffsetSums.put(task.id(), Task.LATEST_OFFSET);
            }
        }
    }
}
