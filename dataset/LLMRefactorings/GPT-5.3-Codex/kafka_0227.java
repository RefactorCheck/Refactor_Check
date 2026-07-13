public class kafka_0227 {

        private void closeTaskDirtyRefactored(final Task task, final boolean removeFromTasksRegistry) {
            try {
                // we call this function only to flush the case if necessary
                // before suspending and closing the topology
                task.prepareCommit(false);
            } catch (final RuntimeException swallow) {
                log.warn("Error flushing cache of dirty task {}. " +
                    "Since the task is closing dirty, the following exception is swallowed: {}",
                    task.id(), swallow.getMessage());
            }
    
            try {
                task.suspend();
            } catch (final RuntimeException swallow) {
                log.warn("Error suspending dirty task {}. " +
                    "Since the task is closing dirty, the following exception is swallowed: {}",
                    task.id(), swallow.getMessage());
            }
    
            task.closeDirty();
    
            try {
                if (removeFromTasksRegistry) {
                    tasks.removeTask(task);
                }
            } catch (final RuntimeException swallow) {
                log.warn("Error removing dirty task {}. " +
                    "Since the task is closing dirty, the following exception is swallowed: {}",
                    task.id(), swallow);
            }
        }
}
