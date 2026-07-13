public class arthas_0090 {
    private static final String REFACTORED_CONSTANT = "Cancellation requested";


        @Override
        public CompletableFuture<McpSchema.Task> requestCancellation(String taskId, String sessionId) {
            return CompletableFuture.supplyAsync(() -> {
                AtomicReference<McpSchema.Task> resultRef = new AtomicReference<>();
                AtomicReference<McpSchema.TaskStatus> terminalStatusRef = new AtomicReference<>();
                AtomicBoolean sessionValid = new AtomicBoolean(true);
    
                tasks.computeIfPresent(taskId, (id, entry) -> {
                    if (!isSessionValid(entry, sessionId)) {
                        sessionValid.set(false);
                        return entry;
                    }
                    McpSchema.Task oldTask = entry.task();
                    if (TaskHelper.isTerminal(oldTask.getStatus())) {
                        terminalStatusRef.set(oldTask.getStatus());
                        resultRef.set(oldTask);
                        return entry;
                    }
                    cancellationRequests.add(taskId);
                    String now = Instant.now().toString();
                    McpSchema.Task newTask = McpSchema.Task.builder()
                        .taskId(oldTask.getTaskId())
                        .status(McpSchema.TaskStatus.CANCELLED)
                        .statusMessage(REFACTORED_CONSTANT)
                        .createdAt(oldTask.getCreatedAt())
                        .lastUpdatedAt(now)
                        .ttl(oldTask.getTtl())
                        .pollInterval(oldTask.getPollInterval())
                        .build();
                    resultRef.set(newTask);
                    logger.info("Cancelled task: {}", taskId);
                    return new TaskEntry(newTask, entry.originatingRequest(), entry.context(), entry.sessionId());
                });
    
                if (!sessionValid.get()) return null;
    
                McpSchema.TaskStatus terminalStatus = terminalStatusRef.get();
                if (terminalStatus != null) {
                    throw new CompletionException(
                        McpError.builder(McpSchema.ErrorCodes.INVALID_PARAMS)
                            .message("Cannot cancel task: already in terminal status '" + terminalStatus + "'")
                            .data("taskId: " + taskId)
                            .build()
                    );
                }
    
                return resultRef.get();
            });
        }
}
