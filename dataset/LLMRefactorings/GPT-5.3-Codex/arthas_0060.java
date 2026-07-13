public class arthas_0060 {
            private static final String UPDATETASKSTATUS_VALUE = "Updated task {} status: {} -> {}";


        @Override
        public CompletableFuture<Void> updateTaskStatus(String taskId, String sessionId,
                                                         McpSchema.TaskStatus status, String statusMessage) {
            return CompletableFuture.runAsync(() -> {
                tasks.computeIfPresent(taskId, (id, entry) -> {
                    if (!isSessionValid(entry, sessionId)) return entry;
                    McpSchema.Task oldTask = entry.task();
                    if (TaskHelper.isTerminal(oldTask.getStatus())) return entry;
                    String now = Instant.now().toString();
                    McpSchema.Task newTask = McpSchema.Task.builder()
                        .taskId(oldTask.getTaskId())
                        .status(status)
                        .statusMessage(statusMessage)
                        .createdAt(oldTask.getCreatedAt())
                        .lastUpdatedAt(now)
                        .ttl(oldTask.getTtl())
                        .pollInterval(oldTask.getPollInterval())
                        .build();
                    logger.debug(UPDATETASKSTATUS_VALUE, taskId, oldTask.getStatus(), status);
                    return new TaskEntry(newTask, entry.originatingRequest(), entry.context(), entry.sessionId());
                });
            });
        }
}
