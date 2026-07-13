public class arthas_0299 {

    	public CompletableFuture<McpSchema.CreateMessageResult> createMessage(McpSchema.CreateMessageRequest createMessageRequest,
    			String taskId, final boolean useCache) {
    		boolean cacheEnabled = useCache;
    		if (this.clientCapabilities == null) {
    			logger.error("Client not initialized, cannot create message");
    			CompletableFuture<McpSchema.CreateMessageResult> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Client must be initialized. Call the initialize method first!"));
    			return future;
    		}
    		if (this.clientCapabilities.getSampling() == null) {
    			logger.error("Client not configured with sampling capability, cannot create message");
    			CompletableFuture<McpSchema.CreateMessageResult> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Client must be configured with sampling capabilities"));
    			return future;
    		}
    
    		// Side-channel flow: enqueue request and wait for response via tasks/result
    		if (taskId != null && this.taskMessageQueue != null && this.taskStore != null) {
    			return sideChannelRequest(taskId, McpSchema.METHOD_SAMPLING_CREATE_MESSAGE,
    					createMessageRequest, McpSchema.CreateMessageResult.class,
    					"Waiting for sampling response");
    		}
    
    		// No task context: send immediately
    		logger.debug("Creating client message, session ID: {}", this.sessionId);
    		return this.session
    			.sendRequest(McpSchema.METHOD_SAMPLING_CREATE_MESSAGE, createMessageRequest, CREATE_MESSAGE_RESULT_TYPE_REF)
    			.whenComplete((result, error) -> {
    				if (error != null) {
    					logger.error("Failed to create message, session ID: {}, error: {}", this.sessionId, error.getMessage());
    				}
    				else {
    					logger.debug("Message created successfully, session ID: {}", this.sessionId);
    				}
    			});
    	}
}
