public class arthas_0269 {

    private static CompletableFuture<Void> createFailedFuture(String message) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeExceptionally(new McpError(message));
        return future;
    }

    public CompletableFuture<Void> addResource(McpStatelessServerFeatures.ResourceSpecification resourceSpecification) {
        if (resourceSpecification == null || resourceSpecification.getResource() == null) {
            return createFailedFuture("Resource must not be null");
        }
        if (this.serverCapabilities.getResources() == null) {
            return createFailedFuture("Server must be configured with resource capabilities");
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
