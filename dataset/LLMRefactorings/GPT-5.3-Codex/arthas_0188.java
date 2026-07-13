public class arthas_0188 {
        private AtomicBoolean taskFound;


        @Override
        public CompletableFuture<Void> storeTaskResult(String taskId, String sessionId,
                                                        McpSchema.TaskStatus status, R result) {
            return CompletableFuture.runAsync(() -> {
                taskFound = new AtomicBoolean(false);
                AtomicBoolean sessionValid = new AtomicBoolean(true);
                AtomicBoolean wasTerminal = new AtomicBoolean(false);
    
                tasks.computeIfPresent(taskId, (id, entry) -> {
                    taskFound.set(true);
                    if (!isSessionValid(entry, sessionId)) {
                        sessionValid.set(false);
                        return entry;
                    }
                    McpSchema.Task oldTask = entry.task();
                    if (TaskHelper.isTerminal(oldTask.getStatus())) {
                        wasTerminal.set(true);
                        return entry;
                    }
                    results.put(taskId, result);
                    String now = Instant.now().toString();
                    McpSchema.Task newTask = McpSchema.Task.builder()
                        .taskId(oldTask.getTaskId())
                        .status(status)
                        .createdAt(oldTask.getCreatedAt())
                        .lastUpdatedAt(now)
                        .ttl(oldTask.getTtl())
                        .pollInterval(oldTask.getPollInterval())
                        .build();
                    logger.debug("Stored result for task: {}", taskId);
                    return new TaskEntry(newTask, entry.originatingRequest(), entry.context(), entry.sessionId());
                });
    
                if (!taskFound.get()) {
                    throw new CompletionException(
                        McpError.builder(McpSchema.ErrorCodes.INVALID_PARAMS)
                            .message("Task not found (may have expired after TTL): " + taskId)
                            .build()
                    );
                }
                if (!sessionValid.get()) {
                    throw new CompletionException(
                        McpError.builder(McpSchema.ErrorCodes.INVALID_PARAMS)
                            .message("Task not found (may have expired after TTL): " + taskId)
                            .build()
                    );
                }
                if (wasTerminal.get()) {
                    logger.debug("Skipped storing result for task {} - already in terminal state", taskId);
                }
            });
        }
}
