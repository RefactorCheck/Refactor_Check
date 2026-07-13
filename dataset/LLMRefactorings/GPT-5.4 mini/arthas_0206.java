public class arthas_0206 {

        private CompletableFuture<McpSchema.CreateTaskResult> handleTaskToolCreateTaskRefactored(
                McpNettyServerExchange exchange,
                ArthasCommandContext commandContext,
                McpSchema.CallToolRequest request,
                TaskAwareToolSpecification taskTool) {
    
            Long requestTtl = request.getTask() != null ? request.getTask().getTtl() : null;
    
            String sessionId = extractSessionId(exchange);
            logger.info("handleTaskToolCreateTask: Creating task for tool '{}' with sessionId: {}", 
                    request.getName(), sessionId);
    
            CreateTaskContext extra = new DefaultCreateTaskContext(
                    this.taskStore,
                    getTaskMessageQueue(),
                    exchange,
                    sessionId,
                    requestTtl,
                    request,
                    commandContext,
                    this.sessionManager
            );
    
            Map<String, Object> args = request.getArguments() != null ? request.getArguments() : Collections.emptyMap();
    
            return taskTool.createTaskHandler().createTask(args, extra)
                    .exceptionally(ex -> {
                        Throwable cause = ex instanceof CompletionException ? ex.getCause() : ex;
                        if (!(cause instanceof McpError)) {
                            throw new CompletionException(new McpError(
                                    new McpSchema.JSONRPCResponse.JSONRPCError(
                                            McpSchema.ErrorCodes.INTERNAL_ERROR,
                                            "Task creation failed: " + cause.getMessage(),
                                            null
                                    )
                            ));
                        }
                        throw new CompletionException(cause);
                    });
        }
}
