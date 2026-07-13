public class arthas_0261 {

    	public static CompletableFuture<Void> addResource(McpServerFeatures.ResourceSpecification resourceSpecification) {
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
    
    		return CompletableFuture.supplyAsync(() -> {
    			if (this.resources.putIfAbsent(resourceSpecification.getResource().getUri(),
    					resourceSpecification) != null) {
    				throw new CompletionException(new McpError(
    						"Resource with URI '" + resourceSpecification.getResource().getUri() + "' already exists"));
    			}
    			logger.debug("Added resource handler: {}", resourceSpecification.getResource().getUri());
    			return null;
    		}).thenCompose(ignored -> {
    			if (this.serverCapabilities.getResources().getListChanged()) {
    				return notifyResourcesListChanged();
    			}
    			return CompletableFuture.completedFuture(null);
    		}).exceptionally(ex -> {
    			Throwable cause = ex instanceof CompletionException ? ex.getCause() : ex;
    			logger.error("Error while adding resource '{}'", resourceSpecification.getResource().getUri(), cause);
    			throw new CompletionException(cause);
    		});
    	}
}
