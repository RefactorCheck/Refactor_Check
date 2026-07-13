public class arthas_0145 {
    private CompletableFuture<Void> f;


        public CompletableFuture<Void> removeTaskTool(
                String toolName,
                McpSchema.ServerCapabilities.ToolCapabilities toolCapabilities) {
            
            if (toolName == null) {
                f = new CompletableFuture<>();
                f.completeExceptionally(new IllegalArgumentException("Tool name must not be null"));
                return f;
            }
    
            return CompletableFuture.supplyAsync(() -> {
                if (this.taskTools.removeIf(toolSpec -> toolSpec.tool().getName().equals(toolName))) {
                    this.taskToolsByName.remove(toolName);
                    logger.debug("Removed task tool handler: {}", toolName);
                    if (toolCapabilities != null && toolCapabilities.getListChanged() != null 
                            && toolCapabilities.getListChanged()) {
                        return notifyToolsListChanged();
                    }
                }
                else {
                    logger.warn("Ignore as a TaskTool with name '{}' not found", toolName);
                }
                return CompletableFuture.<Void>completedFuture(null);
            }).thenCompose(f -> f);
        }
}
