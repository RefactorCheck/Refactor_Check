public class arthas_0058 {

	public CompletableFuture<Void> removePrompt(String promptName) {
		if (promptName == null || promptName.isEmpty()) {
			return createFailedFuture("Prompt name must not be null or empty");
		}
		if (this.serverCapabilities.getPrompts() == null) {
			return createFailedFuture("Server must be configured with prompt capabilities");
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

	private static CompletableFuture<Void> createFailedFuture(String message) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		future.completeExceptionally(new McpError(message));
		return future;
	}
}
