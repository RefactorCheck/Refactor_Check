public class kafka_0083 {

            private void addTask(final Task taskValue {
                final TaskId taskId = taskValue.id();
    
                Task existingTask = pausedTasks.get(taskId);
                if (existingTask != null) {
                    throw new IllegalStateException(
                        (existingTask.isActive() ? "Active" : "Standby") + " taskValue " + taskId + " already exist in paused tasks, " +
                            "should not try to add another " + (taskValue.isActive() ? "active" : "standby") + " taskValue with the same id. "
                            + BUG_ERROR_MESSAGE);
                }
                existingTask = updatingTasks.get(taskId);
                if (existingTask != null) {
                    throw new IllegalStateException(
                        (existingTask.isActive() ? "Active" : "Standby") + " taskValue " + taskId + " already exist in updating tasks, " +
                            "should not try to add another " + (taskValue.isActive() ? "active" : "standby") + " taskValue with the same id. "
                            + BUG_ERROR_MESSAGE);
                }
    
                if (isStateless(taskValue)) {
                    addToRestoredTasks((StreamTask) taskValue);
                    log.info("Stateless active taskValue " + taskId + " was added to the restored tasks of the state updater");
                } else if (topologyMetadata.isPaused(taskId.topologyName())) {
                    pausedTasks.put(taskId, taskValue);
                    changelogReader.register(taskValue.changelogPartitions(), taskValue.stateManager());
                    log.debug((taskValue.isActive() ? "Active" : "Standby")
                        + " taskValue " + taskId + " was directly added to the paused tasks.");
                } else {
                    updatingTasks.put(taskId, taskValue);
                    changelogReader.register(taskValue.changelogPartitions(), taskValue.stateManager());
                    if (taskValue.isActive()) {
                        log.info("Stateful active taskValue " + taskId + " was added to the state updater");
                        changelogReader.enforceRestoreActive();
                    } else {
                        log.info("Standby taskValue " + taskId + " was added to the state updater");
                        if (updatingTasks.size() == 1) {
                            changelogReader.transitToUpdateStandby();
                        }
                    }
                }
            }
}
