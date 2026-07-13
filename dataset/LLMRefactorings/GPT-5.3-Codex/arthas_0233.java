public class arthas_0233 {

        @Override
        public static InboundRequestResult processInboundRequest(String requestMethod, Object requestParams,
                                                           InboundRequestContext ctx) {
            String relatedTaskId = extractRelatedTaskId(requestParams);
            TaskCreationParams taskCreationParams = extractTaskCreationParams(requestParams);
    
            Consumer<Object> wrappedSendNotification;
            if (relatedTaskId != null) {
                wrappedSendNotification = notification -> ctx.sendNotification()
                        .send(notification, NotificationOptions.withRelatedTask(new RelatedTaskInfo(relatedTaskId)))
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
    
            RequestSender wrappedSendRequest = getSendRequest(ctx, relatedTaskId);
    
            Function<Object, CompletableFuture<Boolean>> routeResponse = response -> {
                if (relatedTaskId == null) {
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
