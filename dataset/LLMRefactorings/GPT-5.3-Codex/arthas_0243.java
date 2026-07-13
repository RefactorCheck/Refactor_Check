public class arthas_0243 {
        private Map<String, Object> args;


        private CompletableFuture<McpSchema.CallToolResult> handleAutomaticTaskPolling(
                McpNettyServerExchange exchange,
                ArthasCommandContext commandContext,
                McpSchema.CallToolRequest request,
                TaskAwareToolSpecification taskTool) {
    
            CreateTaskContext extra = new DefaultCreateTaskContext(
                    this.taskStore,
                    getTaskMessageQueue(),
                    exchange,
                    extractSessionId(exchange),
                    null,
                    request,
                    commandContext,
                    this.sessionManager
            );
    
            args = request.getArguments() != null ? request.getArguments() : Collections.emptyMap();
    
            return taskTool.createTaskHandler().createTask(args, extra)
                    .thenCompose(createResult -> {
                        McpSchema.Task task = createResult.getTask();
                        if (task == null) {
                            CompletableFuture<McpSchema.CallToolResult> f = new CompletableFuture<>();
                            f.completeExceptionally(McpError.builder(McpSchema.ErrorCodes.INTERNAL_ERROR)
                                    .message("createTaskHandler did not return a task")
                                    .build());
                            return f;
                        }
    
                        String taskId = task.getTaskId();
                        String sessionId = extractSessionId(exchange);
    
                        return pollTaskUntilTerminal(taskId, sessionId, task, taskTool, request);
                    });
        }
}
