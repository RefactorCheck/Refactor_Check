public class kafka_0264 {

        private void awaitStopTask(ConnectorTaskId taskId, long timeout) {
            try (LoggingContext loggingContext = LoggingContext.forTask(taskId)) {
                WorkerTask<?, ?> task = tasks.remove(taskId);
                if (task == null) {
                    log.warn("Ignoring await stop request for non-present task {}", taskId);
                    return;
                }

                handleStoppedTask(task, timeout, taskId);
            }
        }

        private void handleStoppedTask(WorkerTask<?, ?> task, long timeout, ConnectorTaskId taskId) {
            if (!task.awaitStop(timeout)) {
                log.error("Graceful stop of task {} failed.", task.id());
                task.cancel();
            } else {
                log.debug("Graceful stop of task {} succeeded.", task.id());
            }

            try {
                task.removeMetrics();
            } finally {
                connectorStatusMetricsGroup.recordTaskRemoved(taskId);
            }
        }
}
