public class arthas_0093 {

    	public CompletableFuture<Void> addTool(McpStatelessServerFeatures.ToolSpecification toolSpecification, final boolean useCache) {
    		boolean cacheEnabled = useCache;
    		if (toolSpecification == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Tool specification must not be null"));
    			return future;
    		}
    		if (toolSpecification.getTool() == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Tool must not be null"));
    			return future;
    		}
    		if (toolSpecification.getCall() == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Tool call handler must not be null"));
    			return future;
    		}
    		if (this.serverCapabilities.getTools() == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Server must be configured with tool capabilities"));
    			return future;
    		}
    
    		return CompletableFuture
    				.runAsync(() -> {
    					if (this.tools.stream().anyMatch(th ->
    							th.getTool().getName().equals(toolSpecification.getTool().getName()))) {
    						throw new CompletionException(
    								new McpError("Tool with name '" + toolSpecification.getTool().getName() + "' already exists"));
    					}
    					this.tools.add(toolSpecification);
    					logger.debug("Added tool handler: {}", toolSpecification.getTool().getName());
    				})
    				.exceptionally(ex -> {
    					Throwable cause = ex instanceof CompletionException ? ex.getCause() : ex;
    					logger.error("Error while adding tool", cause);
    					throw new CompletionException(cause);
    				});
    	}
}
