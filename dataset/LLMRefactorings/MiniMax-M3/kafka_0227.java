public class kafka_0227 {

        private void closeTaskDirty(final Task task, final boolean removeFromTasksRegistry) {
            swallowDirtyException(task, "flushing cache of dirty task", () -> task.prepareCommit(false));
            swallowDirtyException(task, "suspending dirty task", task::suspend);
    
            task.closeDirty();
    
            if (removeFromTasksRegistry) {
                swallowDirtyException(task, "removing dirty task", () -> tasks.removeTask(task));
            }
        }

        private void swallowDirtyException(final Task task, final String action, final Runnable runnable) {
            try {
                runnable.run();
            } catch (final RuntimeException swallow) {
                log.warn("Error {} {}. Since the task is closing dirty, the following exception is swallowed: {}",
                    action, task.id(), swallow);
            }
        }
}
