public class kafka_0083 {

            private void addTask(final Task task) {
                final TaskId taskId = task.id();
    
                Task existingTask = pausedTasks.get(taskId);
                if (existingTask != null) {
                    throw new IllegalStateException(
                        (existingTask.isActive() ? "Active" : "Standby") + " task " + taskId + " already exist in paused tasks, " +
                            "should not try to add another " + (task.isActive() ? "active" : "standby") + " task with the same id. "
                            + BUG_ERROR_MESSAGE);
                }
                existingTask = updatingTasks.get(taskId);
                if (existingTask != null) {
                    throw new IllegalStateException(
                        (existingTask.isActive() ? "Active" : "Standby") + " task " + taskId + " already exist in updating tasks, " +
                            "should not try to add another " + (task.isActive() ? "active" : "standby") + " task with the same id. "
                            + BUG_ERROR_MESSAGE);
                }
    
                if (isStateless(task)) {
                    addToRestoredTasks((StreamTask) task);
                    log.info("Stateless active task " + taskId + " was added to the restored tasks of the state updater");
                } else if (topologyMetadata.isPaused(taskId.topologyName())) {
                    pausedTasks.put(taskId, task);
                    changelogReader.register(task.changelogPartitions(), task.stateManager());
                    log.debug((task.isActive() ? "Active" : "Standby")
                        + " task " + taskId + " was directly added to the paused tasks.");
                } else {
                    updatingTasks.put(taskId, task);
                    changelogReader.register(task.changelogPartitions(), task.stateManager());
                    if (task.isActive()) {
                        log.info("Stateful active task " + taskId + " was added to the state updater");
                        changelogReader.enforceRestoreActive();
                    } else {
                        log.info("Standby task " + taskId + " was added to the state updater");
                        if (updatingTasks.size() == 1) {
                            changelogReader.transitToUpdateStandby();
                        }
                    }
                }
            }
}
