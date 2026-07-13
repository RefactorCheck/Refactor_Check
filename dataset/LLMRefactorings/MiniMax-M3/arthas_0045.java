public class arthas_0045 {

	public CompletableFuture<Void> removeResource(String resourceUri) {
		if (resourceUri == null) {
			return failedFuture("Resource URI must not be null");
		}
		if (this.serverCapabilities.getResources() == null) {
			return failedFuture("Server must be configured with resource capabilities");
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

	private static CompletableFuture<Void> failedFuture(String message) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		future.completeExceptionally(new McpError(message));
		return future;
	}
}
