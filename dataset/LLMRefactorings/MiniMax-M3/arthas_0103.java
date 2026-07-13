public class arthas_0103 {

    public CompletableFuture<Void> loggingNotification(LoggingMessageNotification loggingMessageNotification, String taskId) {
        if (loggingMessageNotification == null) {
            return createFailedFuture(new McpError("Logging message must not be null"));
        }

        if (this.isNotificationForLevelAllowed(loggingMessageNotification.getLevel())) {
            if (taskId != null && this.taskMessageQueue != null) {
                return sideChannelNotification(taskId, McpSchema.METHOD_NOTIFICATION_MESSAGE,
                        loggingMessageNotification);
            }

            return this.session.sendNotification(McpSchema.METHOD_NOTIFICATION_MESSAGE, loggingMessageNotification)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        logger.error("Failed to send logging notification, level: {}, session ID: {}, error: {}",
                                loggingMessageNotification.getLevel(), this.sessionId, error.getMessage());
                    }
                });
        }
        return CompletableFuture.completedFuture(null);
    }

    private CompletableFuture<Void> createFailedFuture(Throwable throwable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeExceptionally(throwable);
        return future;
    }
}
