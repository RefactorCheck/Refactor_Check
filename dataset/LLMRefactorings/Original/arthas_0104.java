public class arthas_0104 {

        private CompletableFuture<McpSchema.Result> handleGetTaskResult(String requestMethod, Object requestParams,
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
    
            String sessionId = ctx.sessionId();
    
            McpSchema.GetTaskPayloadRequest typedRequest = new McpSchema.GetTaskPayloadRequest(taskId, null);
    
            return this.taskStore.getTask(taskId, sessionId)
                    .thenCompose(storeResult -> {
                        if (storeResult == null) {
                            CompletableFuture<McpSchema.Result> failed = new CompletableFuture<>();
                            failed.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INVALID_PARAMS)
                                    .message("Task not found (may have expired after TTL)")
                                    .data("Task ID: " + taskId)
                                    .build());
                            return failed;
                        }
    
                        McpSchema.Task task = storeResult.task();
    
                        logger.debug("handleGetTaskResult: Task {} status={}, messageQueue={}",
                                taskId, task.getStatus(),
                                this.messageQueue != null ? "present" : "null");
    
                        // Handle INPUT_REQUIRED: process queued side-channel messages first
                        if (task.getStatus() == McpSchema.TaskStatus.INPUT_REQUIRED && this.messageQueue != null) {
                            logger.debug("handleGetTaskResult: Task {} is INPUT_REQUIRED, starting side-channel processing",
                                    taskId);
                            return processQueuedMessagesAndWaitForTerminal(ctx, taskId, sessionId)
                                    .thenCompose(sideChannelResult -> {
                                        return tryCustomHandlerOrDefault(taskId, typedRequest, ctx, sessionId);
                                    });
                        }
    
                        return tryCustomHandlerOrDefault(taskId, typedRequest, ctx, sessionId);
                    });
        }
}
