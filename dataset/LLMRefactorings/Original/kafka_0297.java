public class kafka_0297 {

        @Override
        public void setUncaughtException(final StreamsException exception, final TaskId taskId) {
            executeWithTasksLocked(() -> {
    
                if (!assignedTasks.containsKey(taskId)) {
                    throw new IllegalArgumentException("An uncaught exception can only be set as long as the task is still assigned");
                }
    
                if (uncaughtExceptions.containsKey(taskId)) {
                    throw new IllegalArgumentException("The uncaught exception must be cleared before restarting processing");
                }
    
                uncaughtExceptions.put(taskId, exception);
            });
    
            log.info("Set an uncaught exception of type {} for task {}, with error message: {}",
                exception.getClass().getName(),
                taskId,
                exception.getMessage());
        }
}
