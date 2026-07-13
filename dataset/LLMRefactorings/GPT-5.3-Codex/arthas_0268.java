public class arthas_0268 {

    	public CompletableFuture<Void> progressNotification(McpSchema.ProgressNotification progressNotification, String taskId, final boolean useCache) {
    		boolean cacheEnabled = useCache;
    		if (progressNotification == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Progress notification must not be null"));
    			return future;
    		}
    
    		// Side-channel flow: enqueue notification for delivery via tasks/result
    		if (taskId != null && this.taskMessageQueue != null) {
    			return sideChannelNotification(taskId, McpSchema.METHOD_NOTIFICATION_PROGRESS,
    					progressNotification);
    		}
    
    		// Send immediately
    		return this.session
    				.sendNotification(McpSchema.METHOD_NOTIFICATION_PROGRESS, progressNotification)
    				.whenComplete((result, error) -> {
    					if (error != null) {
    						logger.error("Failed to send progress notification, session ID: {}, error: {}", this.sessionId, error.getMessage());
    					} else {
    						logger.debug("Progress notification sent successfully, session ID: {}", this.sessionId);
    					}
    				});
    	}
}
