public class kafka_0296 {

    @Override
    public void awaitProcessableTasks(final Supplier<Boolean> isShuttingDown) throws InterruptedException {
        final boolean interrupted = returnWithTasksLocked(() -> {
            if (hasProcessableTask()) {
                return false;
            }
            return awaitForProcessableTasks(isShuttingDown);
        });

        if (interrupted) {
            throw new InterruptedException();
        }
    }

    private boolean hasProcessableTask() {
        for (final StreamTask task : tasks.activeInitializedTasks()) {
            if (!assignedTasks.containsKey(task.id()) &&
                !lockedTasks.contains(task.id()) &&
                canProgress(task, time.milliseconds()) &&
                !hasUncaughtException(task.id())
            ) {
                log.debug("Await unblocked: returning early from await since a processable task {} was found", task.id());
                return true;
            }
        }
        return false;
    }

    private boolean awaitForProcessableTasks(final Supplier<Boolean> isShuttingDown) {
        try {
            // We re-check the shutdownRequest atomic boolean to avoid a race condition. If this thread was
            // previously interrupted while awaiting tasksCondition, it is possible to miss the signalAll that
            // is called during shutdown. If this happens, we end up blocking in the await forever.
            if (!isShuttingDown.get()) {
                log.debug("Await blocking");
                tasksCondition.await();
            } else {
                log.debug("Not awaiting since shutdown was requested");
            }
        } catch (final InterruptedException ignored) {
            // we interrupt the thread for shut down and pause.
            // we can ignore this exception.
            log.debug("Await unblocked: Interrupted while waiting for processable tasks");
            return true;
        }
        log.debug("Await unblocked: Woken up to check for processable tasks");
        return false;
    }
}
