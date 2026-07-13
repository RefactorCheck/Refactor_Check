public class arthas_0046 {

        @Override
        public <T extends McpSchema.Result> CompletableFuture<T> invokeCustomTaskHandler(
                String taskId, String method, McpSchema.Request request,
                TaskHandlerContext context, Class<T> resultType) {
    
            if (this.taskStore == null) {
                return CompletableFuture.completedFuture(null);
            }
            return this.taskStore.getTask(taskId, context.sessionId())
                    .thenCompose(storeResult -> {
                        if (storeResult == null) {
                            logger.debug("invokeCustomTaskHandler: task not found for taskId={}", taskId);
                            return CompletableFuture.completedFuture(null);
                        }
                        return findAndInvokeCustomHandler(storeResult, method, request, context, resultType);
                    })
                    .exceptionally(ex -> {
                        logger.debug("invokeCustomTaskHandler: task lookup failed for taskId={}, returning null",
                                taskId, ex);
                        return null;
                    });
        }
}
