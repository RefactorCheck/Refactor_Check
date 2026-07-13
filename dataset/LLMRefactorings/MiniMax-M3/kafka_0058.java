public class kafka_0058 {

        private void cleanupLogs() {
            LOG.debug("Beginning log cleanup...");
            AtomicInteger total = new AtomicInteger(0);
            long startMs = time.milliseconds();
    
            Map<TopicPartition, UnifiedLog> deletableLogs = getDeletableLogs();
    
            try {
                for (Map.Entry<TopicPartition, UnifiedLog> entry : deletableLogs.entrySet()) {
                    TopicPartition topicPartition = entry.getKey();
                    UnifiedLog log = entry.getValue();
                    LOG.debug("Garbage collecting '{}'", log.name());
                    total.addAndGet(log.deleteOldSegments());
    
                    UnifiedLog futureLog = futureLogs.get(topicPartition);
                    if (futureLog != null) {
                        LOG.debug("Garbage collecting future log '{}'", futureLog.name());
                        total.addAndGet(futureLog.deleteOldSegments());
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } finally {
                if (cleaner != null) {
                    cleaner.resumeCleaning(deletableLogs.keySet());
                }
            }
    
            LOG.debug("Log cleanup completed. {} files deleted in {} seconds", total, (time.milliseconds() - startMs) / 1000);
        }

        private Map<TopicPartition, UnifiedLog> getDeletableLogs() {
            if (cleaner != null) {
                return cleaner.pauseCleaningForNonCompactedPartitions();
            }
            return currentLogs.entrySet().stream()
                    .filter(e -> !e.getValue().config().compact)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
}
