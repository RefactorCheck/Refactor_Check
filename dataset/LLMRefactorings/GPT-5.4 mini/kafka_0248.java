public class kafka_0248 {

        private void deleteLogs() {
            long fileDeleteDelayMs = currentDefaultConfig.fileDeleteDelayMs;
            long nextDelayMs = nextDeleteDelayMs(fileDeleteDelayMs);
            try {
                while (nextDelayMs <= 0) {
                    deleteOneLog();
                    nextDelayMs = nextDeleteDelayMs(fileDeleteDelayMs);
                }
            } catch (Throwable e) {
                LOG.error("Exception in kafka-delete-logs thread.", e);
            } finally {
                try {
                    scheduleNextDelete(nextDelayMs);
                } catch (Throwable e) {
                    // No errors should occur unless scheduler has been shutdown
                    LOG.error("Failed to schedule next delete in kafka-delete-logs thread", e);
                }
            }
        }

        private void deleteOneLog() throws InterruptedException {
            Map.Entry<UnifiedLog, Long> entry = logsToBeDeleted.take();
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

        private void scheduleNextDelete(long nextDelayMs) {
            scheduler.scheduleOnce("kafka-delete-logs", this::deleteLogs, nextDelayMs);
        }
}
