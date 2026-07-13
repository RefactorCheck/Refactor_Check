public class arthas_0129 {

        private CompletableFuture<McpSchema.Result> handleGetTask(String requestMethod, Object requestParams,
                                                                   TaskManagerHost.TaskHandlerContext ctx) {
            if (this.taskStore == null) {
                return createFailedFuture(McpSchema.ErrorCodes.INTERNAL_ERROR, "TaskStore not configured", null);
            }

            String taskId = extractTaskIdFromParams(requestParams);
            if (taskId == null) {
                return createFailedFuture(McpSchema.ErrorCodes.INVALID_PARAMS, "Missing required parameter: taskId", null);
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
                                        return createFailedFuture(McpSchema.ErrorCodes.INVALID_PARAMS,
                                                "Task not found (may have expired after TTL)",
                                                "Task ID: " + taskId);
                                    }
                                    return CompletableFuture.completedFuture(
                                            (McpSchema.Result) McpSchema.GetTaskResult.fromTask(storeResult.task()));
                                });
                    });
        }

        private CompletableFuture<McpSchema.Result> createFailedFuture(McpSchema.ErrorCodes errorCode, String message, String data) {
            CompletableFuture<McpSchema.Result> failed = new CompletableFuture<>();
            McpError.Builder builder = McpError.builder(errorCode).message(message);
            if (data != null) {
                builder.data(data);
            }
            failed.completeExceptionally(builder.build());
            return failed;
        }
}
