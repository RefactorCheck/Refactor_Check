public class arthas_0013 {

	public CompletableFuture<Void> addPrompt(McpStatelessServerFeatures.PromptSpecification promptSpecification) {
		if (promptSpecification == null) {
			return createFailedFuture(new McpError("Prompt specification must not be null"));
		}
		if (this.serverCapabilities.getPrompts() == null) {
			return createFailedFuture(new McpError("Server must be configured with prompt capabilities"));
		}

		return CompletableFuture
				.runAsync(() -> {
					String name = promptSpecification.getPrompt().getName();
					McpStatelessServerFeatures.PromptSpecification existing =
							this.prompts.putIfAbsent(name, promptSpecification);
					if (existing != null) {
						throw new CompletionException(new McpError("Prompt with name '" + name + "' already exists"));
					}
					logger.debug("Added prompt handler: {}", name);
				})
				.exceptionally(ex -> {
					Throwable cause = (ex instanceof CompletionException) ? ex.getCause() : ex;
					String name = promptSpecification.getPrompt().getName();
					logger.error("Error while adding prompt '{}'", name, cause);
					throw new CompletionException(cause);
				});
	}

	private CompletableFuture<Void> createFailedFuture(McpError error) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		future.completeExceptionally(error);
		return future;
	}
}
