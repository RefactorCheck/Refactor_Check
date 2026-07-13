public class arthas_0104 {

    private CompletableFuture<McpSchema.Result> createFailedFuture(McpSchema.ErrorCodes errorCode, String message) {
        CompletableFuture<McpSchema.Result> failed = new CompletableFuture<>();
        failed.completeExceptionally(McpError.builder(errorCode)
                .message(message)
                .build());
        return failed;
    }

    private CompletableFuture<McpSchema.Result> handleGetTaskResult(String requestMethod, Object requestParams,
                                                                          TaskManagerHost.TaskHandlerContext ctx) {
        if (this.taskStore == null) {
            return createFailedFuture(McpSchema.ErrorCodes.INTERNAL_ERROR, "TaskStore not configured");
        }
    
        String taskId = extractTaskIdFromParams(requestParams);
        if (taskId == null) {
            return createFailedFuture(McpSchema.ErrorCodes.INVALID_PARAMS, "Missing required parameter: taskId");
        }
    
        String sessionId = ctx.sessionId();
    
        McpSchema.GetTaskPayloadRequest typedRequest = new McpSchema.GetTaskPayloadRequest(taskId, null);
    
        return this.taskStore.getTask(taskId, sessionId)
                    .thenCompose(storeResult -> {
                        if (storeResult == null) {
                            return createFailedFutureWithData(McpSchema.ErrorCodes.INVALID_PARAMS,
                                    "Task not found (may have expired after TTL)",
                                    "Task ID: " + taskId);
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
