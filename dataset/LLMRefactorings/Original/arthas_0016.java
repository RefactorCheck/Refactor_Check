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
                    CompletableFuture<McpSchema.CreateTaskResult> f = new CompletableFuture<>();
                    f.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INVALID_REQUEST)
                            .message("Server does not support tasks")
                            .data("Task store not configured")
                            .build());
                    return f;
                }
                return handleTaskToolCreateTask(exchange, commandContext, request, taskTool);
            }
    
            if (taskSupportMode == McpSchema.TaskSupportMode.REQUIRED) {
                CompletableFuture<McpSchema.CallToolResult> f = new CompletableFuture<>();
                f.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INVALID_PARAMS)
                        .message("This tool requires task-augmented execution")
                        .data("Tool '" + request.getName() + "' requires task metadata in the request")
                        .build());
                return f;
            }
    
            if (getTaskStore() != null) {
                return handleAutomaticTaskPolling(exchange, commandContext, request, taskTool);
            }
    
            if (taskTool.callHandler() != null) {
                return taskTool.callHandler().apply(exchange, commandContext, request);
            }
    
            CompletableFuture<McpSchema.CallToolResult> f = new CompletableFuture<>();
            f.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INTERNAL_ERROR)
                    .message("Tool requires task store or callHandler for execution")
                    .build());
            return f;
        }
}
