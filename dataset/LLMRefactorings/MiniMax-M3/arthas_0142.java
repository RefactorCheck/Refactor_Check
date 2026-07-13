public class arthas_0142 {

        private CompletableFuture<McpSchema.Result> watchAndFetchResult(String taskId, String sessionId) {
            long timeoutMs = this.pollTimeout.toMillis();
            return this.taskStore.watchTaskUntilTerminal(taskId, sessionId, timeoutMs)
                    .thenCompose(updates -> {
                        if (updates == null || updates.isEmpty() || !updates.get(updates.size() - 1).isTerminal()) {
                            return failedWithTimeout(taskId);
                        }
                        return fetchTaskResult(taskId, sessionId);
                    })
                    .exceptionally(ex -> {
                        if (ex instanceof TimeoutException || ex.getCause() instanceof TimeoutException) {
                            throw new RuntimeException(createTimeoutError(taskId));
                        }
                        throw new RuntimeException(ex);
                    });
        }

        private CompletableFuture<McpSchema.Result> failedWithTimeout(String taskId) {
            CompletableFuture<McpSchema.Result> failed = new CompletableFuture<>();
            failed.completeExceptionally(createTimeoutError(taskId));
            return failed;
        }

        private McpError createTimeoutError(String taskId) {
            return McpError.builder(McpSchema.ErrorCodes.INTERNAL_ERROR)
                    .message("Task did not complete within timeout")
                    .data("Task ID: " + taskId)
                    .build();
        }
}
