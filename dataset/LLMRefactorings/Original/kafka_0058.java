public class kafka_0058 {

        private void cleanupLogs() {
            LOG.debug("Beginning log cleanup...");
            AtomicInteger total = new AtomicInteger(0);
            long startMs = time.milliseconds();
    
            // clean current logs.
            Map<TopicPartition, UnifiedLog> deletableLogs;
            if (cleaner != null) {
                // prevent cleaner from working on same partitions when changing cleanup policy
                deletableLogs = cleaner.pauseCleaningForNonCompactedPartitions();
            } else {
                deletableLogs = currentLogs.entrySet().stream()
                        .filter(e -> !e.getValue().config().compact)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
    
            try {
                for (Map.Entry<TopicPartition, UnifiedLog> entry : deletableLogs.entrySet()) {
                    TopicPartition topicPartition = entry.getKey();
                    UnifiedLog log = entry.getValue();
                    LOG.debug("Garbage collecting '{}'", log.name());
                    total.addAndGet(log.deleteOldSegments());
    
                    UnifiedLog futureLog = futureLogs.get(topicPartition);
                    if (futureLog != null) {
                        // clean future logs
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
}
