public class arthas_0016 {

        private CompletableFuture<?> doHandleTaskToolCall(
                McpNettyServerExchange exchange,
                ArthasCommandContext commandContext,
                McpSchema.CallToolRequest request,
                TaskAwareToolSpecification taskTool) {
    
            McpSchema.ToolExecution execution = taskTool.tool().getExecution();
            McpSchema.TaskSupportMode taskSupportMode = execution != null ? execution.getTaskSupport() : null;
    
            if (request.getTask() != null) {
                if (getTaskStore() == null) {
                    return failedFuture(McpSchema.ErrorCodes.INVALID_REQUEST,
                            "Server does not support tasks",
                            "Task store not configured");
                }
                return handleTaskToolCreateTask(exchange, commandContext, request, taskTool);
            }
    
            if (taskSupportMode == McpSchema.TaskSupportMode.REQUIRED) {
                return failedFuture(McpSchema.ErrorCodes.INVALID_PARAMS,
                        "This tool requires task-augmented execution",
                        "Tool '" + request.getName() + "' requires task metadata in the request");
            }
    
            if (getTaskStore() != null) {
                return handleAutomaticTaskPolling(exchange, commandContext, request, taskTool);
            }
    
            if (taskTool.callHandler() != null) {
                return taskTool.callHandler().apply(exchange, commandContext, request);
            }
    
            return failedFuture(McpSchema.ErrorCodes.INTERNAL_ERROR,
                    "Tool requires task store or callHandler for execution",
                    null);
        }
    
        private static <T> CompletableFuture<T> failedFuture(String errorCode, String message, String data) {
            CompletableFuture<T> future = new CompletableFuture<>();
            McpError.Builder builder = McpError.builder(errorCode).message(message);
            if (data != null) {
                builder.data(data);
            }
            future.completeExceptionally(builder.build());
            return future;
        }
}
