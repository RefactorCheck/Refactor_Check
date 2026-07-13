public class kafka_0186 {

        public Map<TaskId, Long> taskOffsetSums() {

            final Map<TaskId, Task> tasks = allTasks();
            final Set<TaskId> lockedTaskDirectoriesOfNonOwnedTasksAndClosedAndCreatedTasks =
                union(HashSet::new, lockedTaskDirectories, tasks.keySet());

            final Map<TaskId, Long> taskOffsetSums = stateDirectory.taskOffsetSums(lockedTaskDirectoriesOfNonOwnedTasksAndClosedAndCreatedTasks);

            for (final Task task : tasks.values()) {
                final boolean activeLoggedTask = task.isActive() && task.state() == State.RUNNING && !task.changelogPartitions().isEmpty();
                if (activeLoggedTask) {
                    taskOffsetSums.put(task.id(), Task.LATEST_OFFSET);
                }
            }

            return taskOffsetSums;
        }
}
