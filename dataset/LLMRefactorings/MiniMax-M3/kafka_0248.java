public class kafka_0248 {

        private void deleteLogs() {
            long fileDeleteDelayMs = currentDefaultConfig.fileDeleteDelayMs;
            long nextDelayMs = nextDeleteDelayMs(fileDeleteDelayMs);
            try {
                while (nextDelayMs <= 0) {
                    Map.Entry<UnifiedLog, Long> entry = logsToBeDeleted.take();
                    deleteLogEntry(entry);
                    nextDelayMs = nextDeleteDelayMs(fileDeleteDelayMs);
                }
            } catch (Throwable e) {
                LOG.error("Exception in kafka-delete-logs thread.", e);
            } finally {
                try {
                    scheduler.scheduleOnce("kafka-delete-logs", this::deleteLogs, nextDelayMs);
                } catch (Throwable e) {
                    LOG.error("Failed to schedule next delete in kafka-delete-logs thread", e);
                }
            }
        }

        private void deleteLogEntry(Map.Entry<UnifiedLog, Long> entry) {
            UnifiedLog removedLog = entry.getKey();
            if (removedLog != null) {
                try {
                    removedLog.delete();
                    LOG.info("Deleted log for partition {} in {}.", removedLog.topicPartition(), removedLog.dir().getAbsolutePath());
                } catch (KafkaStorageException kse) {
                    LOG.error("Exception while deleting {} in dir {}.", removedLog, removedLog.parentDir(), kse);
                }
            }
        }
}
