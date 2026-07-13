public class arthas_0060 {

        @Override
        public CompletableFuture<Void> updateTaskStatus(String taskId, String sessionId,
                                                         McpSchema.TaskStatus status, String statusMessage) {
            return CompletableFuture.runAsync(() -> {
                tasks.computeIfPresent(taskId, (id, entry) -> {
                    if (!isSessionValid(entry, sessionId)) return entry;
                    McpSchema.Task oldTask = entry.task();
                    if (TaskHelper.isTerminal(oldTask.getStatus())) return entry;
                    McpSchema.Task newTask = createUpdatedTask(oldTask, status, statusMessage);
                    logger.debug("Updated task {} status: {} -> {}", taskId, oldTask.getStatus(), status);
                    return new TaskEntry(newTask, entry.originatingRequest(), entry.context(), entry.sessionId());
                });
            });
        }

        private McpSchema.Task createUpdatedTask(McpSchema.Task oldTask, McpSchema.TaskStatus status, String statusMessage) {
            String now = Instant.now().toString();
            return McpSchema.Task.builder()
                .taskId(oldTask.getTaskId())
                .status(status)
                .statusMessage(statusMessage)
                .createdAt(oldTask.getCreatedAt())
                .lastUpdatedAt(now)
                .ttl(oldTask.getTtl())
                .pollInterval(oldTask.getPollInterval())
                .build();
        }
}
