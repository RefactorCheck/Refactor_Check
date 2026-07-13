public class arthas_0036 {

        @SuppressWarnings("unchecked")
        private CompletableFuture<McpSchema.Result> fetchTaskResult(String taskId, String sessionId) {
            // Re-fetch the task to get its current status for fallback construction.
            return this.taskStore.getTask(taskId, sessionId).thenCompose(storeResult -> {
                final McpSchema.Task task = storeResult != null ? storeResult.task() : null;
                TaskStore<McpSchema.Result> store = (TaskStore<McpSchema.Result>) this.taskStore;
                return store.getTaskResult(taskId, sessionId)
                        .thenApply(result -> {
                            if (result != null) {
                                return result;
                            }
                            // CANCELLED tasks never store a payload — construct a semantic response.
                            if (task != null && task.getStatus() == McpSchema.TaskStatus.CANCELLED) {
                                return buildCancelledResult(task);
                            }
                            // Should not reach here for FAILED tasks (payload stored by failTask).
                            throw new RuntimeException("Task result not found");
                        });
            });
        }

        private McpSchema.Result buildCancelledResult(McpSchema.Task task) {
            String msg = "Task was cancelled" +
                    (task.getStatusMessage() != null ? ": " + task.getStatusMessage() : "");
            return (McpSchema.Result) new McpSchema.CallToolResult(msg, true, null);
        }
}
