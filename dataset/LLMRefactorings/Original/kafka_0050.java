public class kafka_0050 {

        boolean handleCorruption(final Set<TaskId> corruptedTasks) {
            final Set<TaskId> activeTasks = new HashSet<>(tasks.activeInitializedTaskIds());
    
            // We need to stop all processing, since we need to commit non-corrupted tasks as well.
            maybeLockTasks(activeTasks);
    
            final Set<Task> corruptedActiveTasks = new TreeSet<>(Comparator.comparing(Task::id));
            final Set<Task> corruptedStandbyTasks = new TreeSet<>(Comparator.comparing(Task::id));
    
            for (final TaskId taskId : corruptedTasks) {
                final Task task = tasks.initializedTask(taskId);
                if (task.isActive()) {
                    corruptedActiveTasks.add(task);
                } else {
                    corruptedStandbyTasks.add(task);
                }
            }
    
            // Make sure to clean up any corrupted standby tasks in their entirety before committing
            // since TaskMigrated can be thrown and the resulting handleLostAll will only clean up active tasks
            closeDirtyAndRevive(corruptedStandbyTasks, true);
    
            // We need to commit before closing the corrupted active tasks since this will force the ongoing txn to abort
            try {
                final Collection<Task> tasksToCommit = tasks.allInitializedTasksPerId()
                    .values()
                    .stream()
                    .filter(t -> t.state() == Task.State.RUNNING)
                    .filter(t -> !corruptedTasks.contains(t.id()))
                    .collect(Collectors.toSet());
                commitTasksAndMaybeUpdateCommittableOffsets(tasksToCommit, new HashMap<>());
            } catch (final TaskCorruptedException e) {
                log.info("Some additional tasks were found corrupted while trying to commit, these will be added to the " +
                             "tasks to clean and revive: {}", e.corruptedTasks());
                corruptedActiveTasks.addAll(tasks.initializedTasks(e.corruptedTasks()));
            } catch (final TimeoutException e) {
                log.info("Hit TimeoutException when committing all non-corrupted tasks, these will be closed and revived");
                final Collection<Task> uncorruptedTasks = new HashSet<>(tasks.activeInitializedTasks());
                uncorruptedTasks.removeAll(corruptedActiveTasks);
                // Those tasks which just timed out can just be closed dirty without marking changelogs as corrupted
                closeDirtyAndRevive(uncorruptedTasks, false);
            }
    
            closeDirtyAndRevive(corruptedActiveTasks, true);
    
            maybeUnlockTasks(activeTasks);
    
            return !corruptedActiveTasks.isEmpty();
        }
}
