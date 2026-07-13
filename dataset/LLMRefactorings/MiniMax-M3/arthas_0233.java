public class arthas_0233 {

    @Override
    public InboundRequestResult processInboundRequest(String requestMethod, Object requestParams,
                                                       InboundRequestContext ctx) {
        String relatedTaskId = extractRelatedTaskId(requestParams);
        TaskCreationParams taskCreationParams = extractTaskCreationParams(requestParams);

        Consumer<Object> wrappedSendNotification = createNotificationSender(ctx, relatedTaskId);

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

    private Consumer<Object> createNotificationSender(InboundRequestContext ctx, String relatedTaskId) {
        NotificationOptions options = relatedTaskId != null
                ? NotificationOptions.withRelatedTask(new RelatedTaskInfo(relatedTaskId))
                : NotificationOptions.empty();
        return notification -> ctx.sendNotification()
                .send(notification, options)
                .exceptionally(ex -> {
                    logger.warn("Failed to send notification", ex);
                    return null;
                });
    }
}
