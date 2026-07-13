public class arthas_0261 {

    	public CompletableFuture<Void> addResource(McpServerFeatures.ResourceSpecification resourceSpecification) {
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

    		String resourceUri = resourceSpecification.getResource().getUri();
    		return CompletableFuture.supplyAsync(() -> {
    			if (this.resources.putIfAbsent(resourceUri, resourceSpecification) != null) {
    				throw new CompletionException(new McpError(
    						"Resource with URI '" + resourceUri + "' already exists"));
    			}
    			logger.debug("Added resource handler: {}", resourceUri);
    			return null;
    		}).thenCompose(ignored -> {
    			if (this.serverCapabilities.getResources().getListChanged()) {
    				return notifyResourcesListChanged();
    			}
    			return CompletableFuture.completedFuture(null);
    		}).exceptionally(ex -> {
    			Throwable cause = ex instanceof CompletionException ? ex.getCause() : ex;
    			logger.error("Error while adding resource '{}'", resourceUri, cause);
    			throw new CompletionException(cause);
    		});
    	}
}
