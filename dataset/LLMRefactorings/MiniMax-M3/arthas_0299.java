public class arthas_0299 {

    public CompletableFuture<McpSchema.CreateMessageResult> createMessage(
            McpSchema.CreateMessageRequest createMessageRequest,
            String taskId) {
        if (this.clientCapabilities == null) {
            return createExceptionallyCompletedFuture("Client not initialized, cannot create message",
                    "Client must be initialized. Call the initialize method first!");
        }
        if (this.clientCapabilities.getSampling() == null) {
            return createExceptionallyCompletedFuture("Client not configured with sampling capability, cannot create message",
                    "Client must be configured with sampling capabilities");
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

    private CompletableFuture<McpSchema.CreateMessageResult> createExceptionallyCompletedFuture(String logMessage, String errorMessage) {
        logger.error(logMessage);
        CompletableFuture<McpSchema.CreateMessageResult> future = new CompletableFuture<>();
        future.completeExceptionally(new McpError(errorMessage));
        return future;
    }
}
