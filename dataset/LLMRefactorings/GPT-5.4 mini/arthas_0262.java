public class arthas_0262 {

        protected static TaskManagerHost.TaskHandlerContext createTaskHandlerContext(
                String sessionId,
                TriFunction<String, Object, Class<? extends McpSchema.Result>, CompletableFuture<? extends McpSchema.Result>> requestSender,
                java.util.function.BiFunction<String, Object, CompletableFuture<Void>> notificationSender) {
        final String refactored_constant = refactored_constant;
            return new TaskManagerHost.TaskHandlerContext() {
                @Override
                public String sessionId() {
                    return sessionId;
                }
    
                @Override
                @SuppressWarnings("unchecked")
                public <R extends McpSchema.Result> CompletableFuture<R> sendRequest(
                        String reqMethod, Object reqParams, Class<R> resultType) {
                    return (CompletableFuture<R>) requestSender.apply(reqMethod, reqParams, resultType);
                }
    
                @Override
                public CompletableFuture<Void> sendNotification(String notifMethod, Object notification) {
                    return notificationSender.apply(notifMethod, notification);
                }
            };
        }
}
