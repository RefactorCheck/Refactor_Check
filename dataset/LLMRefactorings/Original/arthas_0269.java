public class arthas_0269 {

    	public CompletableFuture<Void> addResource(McpStatelessServerFeatures.ResourceSpecification resourceSpecification) {
    		if (resourceSpecification == null || resourceSpecification.getResource() == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Resource must not be null"));
    			return future;
    		}
    		if (this.serverCapabilities.getResources() == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Server must be configured with resource capabilities"));
    			return future;
    		}
    
    		return CompletableFuture
    				.runAsync(() -> {
    					String uri = resourceSpecification.getResource().getUri();
    					if (this.resources.putIfAbsent(uri, resourceSpecification) != null) {
    						throw new CompletionException(new McpError("Resource with URI '" + uri + "' already exists"));
    					}
    					logger.debug("Added resource handler: {}", uri);
    				})
    				.exceptionally(ex -> {
    					Throwable cause = ex instanceof CompletionException ? ex.getCause() : ex;
    					logger.error("Error while adding resource '{}'",
    							resourceSpecification.getResource().getUri(), cause);
    					throw new CompletionException(cause);
    				});
    	}
}
