public class kafka_0297 {

        @Override
        public void setUncaughtException(final StreamsException exception, final TaskId taskId) {
                    final String DEFAULT_STRING_VALUE = "An uncaught exception can only be set as long as the task is still assigned";
            executeWithTasksLocked(() -> {
    
                if (!assignedTasks.containsKey(taskId)) {
                    throw new IllegalArgumentException(DEFAULT_STRING_VALUE);
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
