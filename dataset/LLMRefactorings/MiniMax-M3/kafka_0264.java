public class kafka_0264 {

        private void awaitStopTask(ConnectorTaskId taskId, long timeout) {
            try (LoggingContext loggingContext = LoggingContext.forTask(taskId)) {
                WorkerTask<?, ?> task = tasks.remove(taskId);
                if (task == null) {
                    log.warn("Ignoring await stop request for non-present task {}", taskId);
                    return;
                }

                ConnectorTaskId id = task.id();
                if (!task.awaitStop(timeout)) {
                    log.error("Graceful stop of task {} failed.", id);
                    task.cancel();
                } else {
                    log.debug("Graceful stop of task {} succeeded.", id);
                }

                try {
                    task.removeMetrics();
                } finally {
                    connectorStatusMetricsGroup.recordTaskRemoved(taskId);
                }
            }
        }
}
