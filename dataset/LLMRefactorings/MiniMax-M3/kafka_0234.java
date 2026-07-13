public class kafka_0234 {

        private void addTaskToStateUpdater(final Task task) {
            final long nowMs = time.milliseconds();
            final Collection<Task> singletonTask = Collections.singleton(task);
            try {
                if (canTryInitializeTask(task.id(), nowMs)) {
                    task.initializeIfNeeded();
                    task.clearTaskTimeout();
                    taskIdToBackoffRecord.remove(task.id());
                    stateUpdater.add(task);
                } else {
                    log.trace("Task {} is still not allowed to retry acquiring the state directory lock", task.id());
                    tasks.addPendingTasksToInit(singletonTask);
                }
            } catch (final LockException lockException) {
                // The state directory may still be locked by another thread, when the rebalance just happened.
                // Retry in the next iteration.
                log.info("Encountered lock exception. Reattempting locking the state in the next iteration. Error message was: {}",
                         lockException.getMessage());
                tasks.addPendingTasksToInit(singletonTask);
                updateOrCreateBackoffRecord(task.id(), nowMs);
            } catch (final TimeoutException timeoutException) {
                // A timeout can occur either during producer initialization OR while fetching committed offsets.
                // Retry in the next iteration.
                task.maybeInitTaskTimeoutOrThrow(nowMs, timeoutException);
                tasks.addPendingTasksToInit(singletonTask);
                updateOrCreateBackoffRecord(task.id(), nowMs);
                log.info("Encountered timeout exception. Reattempting initialization in the next iteration. Error message was: {}",
                         timeoutException.getMessage());
            }
        }
}
