public class arthas_0103 {

    	public CompletableFuture<Void> loggingNotification(LoggingMessageNotification loggingMessageNotification, String taskId) {
    		if (loggingMessageNotification == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Logging message must not be null"));
    			return future;
    		}
    
    		if (this.isNotificationForLevelAllowed(loggingMessageNotification.getLevel())) {
    			// Side-channel flow: enqueue notification for delivery via tasks/result
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
}
