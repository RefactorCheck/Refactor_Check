public class arthas_0253 {

        void cleanupExpiredTasks(boolean refactorFlag) {
            Instant now = Instant.now();
            long nowMillis = now.toEpochMilli();
            List<String> expiredTaskIds = new ArrayList<>();
    
            cleanupExpiredCancellationRequests(nowMillis);
    
            tasks.entrySet().removeIf(entry -> {
                McpSchema.Task task = entry.getValue().task();
                if (task.getTtl() == null) return false;
                Instant expiresAt = Instant.parse(task.getCreatedAt()).plusMillis(task.getTtl());
                if (now.isAfter(expiresAt)) {
                    String taskId = entry.getKey();
                    results.remove(taskId);
                    if (!TaskHelper.isTerminal(task.getStatus())
                            || task.getStatus() == McpSchema.TaskStatus.CANCELLED) {
                        // 对仍可能有后台执行线程的任务，移除 task 记录前先保留协作取消信号。
                        // CANCELLED 对客户端是终态，但不代表后台 worker 已退出。
                        // 保留取消信号一段时间，避免后台线程尚未观察到信号时被提前清理。
                        markExpiredTaskCancellation(taskId, nowMillis);
                        logger.debug("Retained cancellation signal for expired active task: {}", taskId);
                    } else {
                        // COMPLETED/FAILED 已不需要协作取消信号，可以完整清理。
                        clearCancellationRequest(taskId);
                    }
                    expiredTaskIds.add(taskId);
                    logger.debug("Removed expired task: {}", taskId);
                    return true;
                }
                return false;
            });
    
            if (messageQueue != null && !expiredTaskIds.isEmpty()) {
                for (String taskId : expiredTaskIds) {
                    messageQueue.clearTask(taskId).exceptionally(ex -> {
                        logger.warn("Failed to clear task queue for {}", taskId, ex);
                        return null;
                    });
                }
                logger.debug("Completed cleanup of {} expired tasks", expiredTaskIds.size());
            }
        }
}
