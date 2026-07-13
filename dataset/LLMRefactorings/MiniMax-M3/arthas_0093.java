public class arthas_0093 {

    public CompletableFuture<Void> addTool(McpStatelessServerFeatures.ToolSpecification toolSpecification) {
        if (toolSpecification == null) {
            return failedFuture("Tool specification must not be null");
        }
        if (toolSpecification.getTool() == null) {
            return failedFuture("Tool must not be null");
        }
        if (toolSpecification.getCall() == null) {
            return failedFuture("Tool call handler must not be null");
        }
        if (this.serverCapabilities.getTools() == null) {
            return failedFuture("Server must be configured with tool capabilities");
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

    private CompletableFuture<Void> failedFuture(String message) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.completeExceptionally(new McpError(message));
        return future;
    }
}
