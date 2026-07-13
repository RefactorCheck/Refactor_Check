public class arthas_0241 {

        @Override
        @SuppressWarnings("unchecked")
        protected <T extends McpSchema.Result> CompletableFuture<T> findAndInvokeCustomHandler(
                GetTaskFromStoreResult storeResult, String method, McpSchema.Request request,
                TaskManagerHost.TaskHandlerContext context, Class<T> resultType) {
    
            String toolName = null;
            if (storeResult.originatingRequest() instanceof McpSchema.CallToolRequest) {
                McpSchema.CallToolRequest ctr = (McpSchema.CallToolRequest) storeResult.originatingRequest();
                toolName = ctr.getName();
            }
    
            TaskAwareToolSpecification taskTool = toolName != null ? this.taskToolsByName.get(toolName) : null;
    
            if (taskTool == null) {
                return CompletableFuture.completedFuture(null);
            }
    
            McpNettyServerExchange exchange = new McpNettyServerExchange(context.sessionId(), null, null,
                    null, McpTransportContext.EMPTY, null);
    
            if (McpSchema.METHOD_TASKS_GET.equals(method)) {
                GetTaskHandler handler = taskTool.getTaskHandler();
                if (handler != null && request instanceof McpSchema.GetTaskRequest) {
                    McpSchema.GetTaskRequest getRequest = (McpSchema.GetTaskRequest) request;
                    return handler.handle(exchange, getRequest)
                            .thenApply(result -> resultType.cast(result));
                }
            }
            else if (McpSchema.METHOD_TASKS_RESULT.equals(method)) {
                GetTaskResultHandler handler = taskTool.getTaskResultHandler();
                if (handler != null && request instanceof McpSchema.GetTaskPayloadRequest) {
                    McpSchema.GetTaskPayloadRequest payloadRequest = (McpSchema.GetTaskPayloadRequest) request;
                    return handler.handle(exchange, payloadRequest)
                            .thenApply(result -> resultType.cast(result));
                }
            }
    
            return CompletableFuture.completedFuture(null);
        }
}
