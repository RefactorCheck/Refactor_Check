public class kafka_0186 {

        public Map<TaskId, Long> taskOffsetSums() {
    
            // Not all tasks will create directories, and there may be directories for tasks we don't currently own,
            // so we consider all tasks that are either owned or on disk. This includes stateless tasks, which should
            // just have an empty changelogOffsets map.
            final Map<TaskId, Task> tasks = allTasks();
            final Set<TaskId> lockedTaskDirectoriesOfNonOwnedTasksAndClosedAndCreatedTasks =
                union(HashSet::new, lockedTaskDirectories, tasks.keySet());
    
            final Map<TaskId, Long> taskOffsetSums = stateDirectory.taskOffsetSums(lockedTaskDirectoriesOfNonOwnedTasksAndClosedAndCreatedTasks);
    
            // overlay latest offsets from assigned tasks
            for (final Task task : tasks.values()) {
                // exclude stateless and non-logged tasks
                if (task.isActive() && task.state() == State.RUNNING && !task.changelogPartitions().isEmpty()) {
                    taskOffsetSums.put(task.id(), Task.LATEST_OFFSET);
                }
            }
    
            return taskOffsetSums;
        }
}
