public class arthas_0129 {

        private CompletableFuture<McpSchema.Result> handleGetTask(String requestMethod, Object requestParams,
                                                                   TaskManagerHost.TaskHandlerContext ctx) {
            if (this.taskStore == null) {
                CompletableFuture<McpSchema.Result> failed = new CompletableFuture<>();
                failed.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INTERNAL_ERROR)
                        .message("TaskStore not configured")
                        .build());
                return failed;
            }
    
            String taskId = extractTaskIdFromParams(requestParams);
            if (taskId == null) {
                CompletableFuture<McpSchema.Result> failed = new CompletableFuture<>();
                failed.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INVALID_PARAMS)
                        .message("Missing required parameter: taskId")
                        .build());
                return failed;
            }
    
            McpSchema.GetTaskRequest typedRequest = new McpSchema.GetTaskRequest(taskId, null);
    
            return host.invokeCustomTaskHandler(taskId, McpSchema.METHOD_TASKS_GET, typedRequest, ctx,
                            McpSchema.GetTaskResult.class)
                    .thenCompose(result -> {
                        if (result != null) {
                            return CompletableFuture.completedFuture(result);
                        }
                        return this.taskStore.getTask(taskId, ctx.sessionId())
                                .thenCompose(storeResult -> {
                                    if (storeResult == null) {
                                        CompletableFuture<McpSchema.Result> failed = new CompletableFuture<>();
                                        failed.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INVALID_PARAMS)
                                                .message("Task not found (may have expired after TTL)")
                                                .data("Task ID: " + taskId)
                                                .build());
                                        return failed;
                                    }
                                    return CompletableFuture.completedFuture(
                                            (McpSchema.Result) McpSchema.GetTaskResult.fromTask(storeResult.task()));
                                });
                    });
        }
}
