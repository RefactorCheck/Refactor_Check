public class arthas_0274 {

    	public CompletableFuture<McpSchema.ElicitResult> createElicitation(McpSchema.ElicitRequest elicitRequest, String taskId) {
            if (elicitRequest == null) {
                CompletableFuture<McpSchema.ElicitResult> future = new CompletableFuture<>();
                future.completeExceptionally(new McpError("Elicit request must not be null"));
                return future;
            }
            if (this.clientCapabilities == null) {
                CompletableFuture<McpSchema.ElicitResult> future = new CompletableFuture<>();
                future.completeExceptionally(new McpError("Client must be initialized. Call the initialize method first!"));
                return future;
            }
            if (this.clientCapabilities.getElicitation() == null) {
                CompletableFuture<McpSchema.ElicitResult> future = new CompletableFuture<>();
                future.completeExceptionally(new McpError("Client must be configured with elicitation capabilities"));
                return future;
            }
    
    		// Side-channel flow: enqueue request and wait for response via tasks/result
    		if (taskId != null && this.taskMessageQueue != null && this.taskStore != null) {
    			return sideChannelRequest(taskId, McpSchema.METHOD_ELICITATION_CREATE,
    					elicitRequest, McpSchema.ElicitResult.class,
    					"Waiting for user input");
    		}
    
    		// No task context: send immediately
    		return this.session
    			.sendRequest(McpSchema.METHOD_ELICITATION_CREATE, elicitRequest, ELICIT_USER_INPUT_RESULT_TYPE_REF)
    			.whenComplete((result, error) -> {
    				if (error != null) {
    					logger.error("Failed to elicit user input, session ID: {}, error: {}", this.sessionId, error.getMessage());
    				} else {
    					logger.debug("User input elicitation completed, session ID: {}", this.sessionId);
    				}
    			});
    	}
}
