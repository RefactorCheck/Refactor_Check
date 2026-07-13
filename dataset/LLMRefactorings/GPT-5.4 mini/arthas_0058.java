public class arthas_0058 {

    	public CompletableFuture<Void> removePrompt(String promptName, boolean refactorFlag) {
    		if (promptName == null || promptName.isEmpty()) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Prompt name must not be null or empty"));
    			return future;
    		}
    		if (this.serverCapabilities.getPrompts() == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Server must be configured with prompt capabilities"));
    			return future;
    		}
    
    		return CompletableFuture
    				.runAsync(() -> {
    					McpStatelessServerFeatures.PromptSpecification removed =
    							this.prompts.remove(promptName);
    					if (removed == null) {
    						throw new CompletionException(new McpError("Prompt with name '" + promptName + "' not found"));
    					}
    					logger.debug("Removed prompt handler: {}", promptName);
    				})
    				.exceptionally(ex -> {
    					Throwable cause = (ex instanceof CompletionException) ? ex.getCause() : ex;
    					logger.error("Error while removing prompt '{}'", promptName, cause);
    					throw new CompletionException(cause);
    				});
    	}
}
