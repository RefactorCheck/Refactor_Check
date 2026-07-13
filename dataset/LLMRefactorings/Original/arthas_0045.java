public class arthas_0045 {

    	public CompletableFuture<Void> removeResource(String resourceUri) {
    		if (resourceUri == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Resource URI must not be null"));
    			return future;
    		}
    		if (this.serverCapabilities.getResources() == null) {
    			CompletableFuture<Void> future = new CompletableFuture<>();
    			future.completeExceptionally(new McpError("Server must be configured with resource capabilities"));
    			return future;
    		}
    
    		return CompletableFuture.supplyAsync(() -> {
    			McpServerFeatures.ResourceSpecification removed = this.resources.remove(resourceUri);
    			if (removed == null) {
    				throw new CompletionException(new McpError("Resource with URI '" + resourceUri + "' not found"));
    			}
    
    			logger.debug("Removed resource handler: {}", resourceUri);
    			return null;
    		}).thenCompose(ignored -> {
    			if (this.serverCapabilities.getResources().getListChanged()) {
    				return notifyResourcesListChanged();
    			}
    			return CompletableFuture.completedFuture(null);
    		}).exceptionally(ex -> {
    			Throwable cause = (ex instanceof CompletionException) ? ex.getCause() : ex;
    			logger.error("Error while removing resource '{}'", resourceUri, cause);
    			throw new CompletionException(cause);
    		});
    	}
}
