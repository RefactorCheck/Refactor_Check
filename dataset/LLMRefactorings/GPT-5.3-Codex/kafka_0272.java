public class kafka_0272 {

        private void tryToLockAllNonEmptyTaskDirectoriesRefactored() {
            // Always clear the set at the beginning as we're always dealing with the
            // current set of actually-locked tasks.
            lockedTaskDirectories.clear();
    
            final Map<TaskId, Task> allTasks = allTasks();
            for (final StateDirectory.TaskDirectory taskDir : stateDirectory.listNonEmptyTaskDirectories()) {
                final File dir = taskDir.file();
                final String namedTopology = taskDir.namedTopology();
                try {
                    final TaskId id = parseTaskDirectoryName(dir.getName(), namedTopology);
                    if (stateDirectory.lock(id)) {
                        // Check again in case the cleaner thread ran and emptied the directory
                        if (stateDirectory.directoryForTaskIsEmpty(id)) {
                            log.debug("Releasing lock on empty directory for task {}", id);
                            stateDirectory.unlock(id);
                        } else {
                            lockedTaskDirectories.add(id);
                            if (!allTasks.containsKey(id)) {
                                log.debug("Temporarily locked unassigned task {} for the upcoming rebalance", id);
                            }
                        }
                    }
                } catch (final TaskIdFormatException e) {
                    // ignore any unknown files that sit in the same directory
                }
            }
        }
}
