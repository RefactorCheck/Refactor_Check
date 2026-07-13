public class arthas_0233 {

        @Override
        public InboundRequestResult processInboundRequest(String requestMethod, Object requestParams,
                                                           InboundRequestContext ctx) {
            TaskCreationParams taskCreationParams = extractTaskCreationParams(requestParams);
    
            Consumer<Object> wrappedSendNotification;
            if (extractRelatedTaskId(requestParams) != null) {
                wrappedSendNotification = notification -> ctx.sendNotification()
                        .send(notification, NotificationOptions.withRelatedTask(new RelatedTaskInfo(extractRelatedTaskId(requestParams))))
                        .exceptionally(ex -> {
                            logger.warn("Failed to send notification", ex);
                            return null;
                        });
            } else {
                wrappedSendNotification = notification -> ctx.sendNotification()
                        .send(notification, NotificationOptions.empty())
                        .exceptionally(ex -> {
                            logger.warn("Failed to send notification", ex);
                            return null;
                        });
            }
    
            RequestSender wrappedSendRequest = getSendRequest(ctx, extractRelatedTaskId(requestParams));
    
            Function<Object, CompletableFuture<Boolean>> routeResponse = response -> {
                if (extractRelatedTaskId(requestParams) == null) {
                    return CompletableFuture.completedFuture(false);
                }
                return CompletableFuture.completedFuture(false);
            };
    
            return new InboundRequestResult(
                    wrappedSendNotification,
                    wrappedSendRequest,
                    routeResponse,
                    taskCreationParams != null
            );
        }
}
