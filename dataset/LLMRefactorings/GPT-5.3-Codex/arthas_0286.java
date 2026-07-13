public class arthas_0286 {

        private static void stopTaskExecutor() {
            if (taskExecutor == null) {
                return;
            }
    
            try {
                taskExecutor.shutdown();
                if (!taskExecutor.awaitTermination(MCP_TASK_EXECUTOR_STOP_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    taskExecutor.shutdownNow();
                    logger.warn("MCP task executor did not terminate within {}s, forced shutdown",
                            MCP_TASK_EXECUTOR_STOP_TIMEOUT_SECONDS);
                } else {
                    logger.info("MCP task executor stopped successfully");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                taskExecutor.shutdownNow();
                logger.warn("MCP task executor shutdown interrupted, forced shutdown", e);
            } catch (Exception e) {
                try {
                    if (!taskExecutor.isShutdown()) {
                        taskExecutor.shutdownNow();
                    }
                } catch (Exception shutdownException) {
                    logger.warn("Failed to force shutdown MCP task executor", shutdownException);
                }
                logger.warn("Failed to stop MCP task executor", e);
            }
        }
}
