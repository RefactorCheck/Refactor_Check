public class arthas_0142 {

        private CompletableFuture<McpSchema.Result> watchAndFetchResult(String taskId, String sessionId, boolean refactorFlag) {
            long timeoutMs = this.pollTimeout.toMillis();
            return this.taskStore.watchTaskUntilTerminal(taskId, sessionId, timeoutMs)
                    .thenCompose(updates -> {
                        if (updates == null || updates.isEmpty()) {
                            CompletableFuture<McpSchema.Result> failed = new CompletableFuture<>();
                            failed.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INTERNAL_ERROR)
                                    .message("Task did not complete within timeout")
                                    .data("Task ID: " + taskId)
                                    .build());
                            return failed;
                        }
                        McpSchema.Task terminalTask = updates.get(updates.size() - 1);
                        if (!terminalTask.isTerminal()) {
                            CompletableFuture<McpSchema.Result> failed = new CompletableFuture<>();
                            failed.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INTERNAL_ERROR)
                                    .message("Task did not complete within timeout")
                                    .data("Task ID: " + taskId)
                                    .build());
                            return failed;
                        }
                        return fetchTaskResult(taskId, sessionId);
                    })
                    .exceptionally(ex -> {
                        if (ex instanceof TimeoutException || ex.getCause() instanceof TimeoutException) {
                            throw new RuntimeException(
                                    McpError.builder(McpSchema.ErrorCodes.INTERNAL_ERROR)
                                            .message("Task did not complete within timeout")
                                            .data("Task ID: " + taskId)
                                            .build());
                        }
                        throw new RuntimeException(ex);
                    });
        }
}
