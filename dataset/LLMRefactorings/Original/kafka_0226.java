public class kafka_0226 {

        @Override
        public void close() throws ProcessorStateException {
            log.debug("Closing its state manager and all the registered state stores: {}", stores);
    
            final Map<TopicPartition, Long> allOffsets = new HashMap<>();
            RuntimeException firstException = null;
            // attempting to close the stores, just in case they
            // are not closed by a ProcessorNode yet
            if (!stores.isEmpty()) {
                for (final Map.Entry<String, StateStoreMetadata> entry : stores.entrySet()) {
                    final StateStoreMetadata metadata = entry.getValue();
                    final StateStore store = metadata.stateStore;
                    log.trace("Closing store {}", store.name());
                    try {
                        store.close();
                    } catch (final RuntimeException exception) {
                        if (firstException == null) {
                            // do NOT wrap the error if it is actually caused by Streams itself
                            // In case of FailedProcessingException Do not keep the failed processing exception in the stack trace
                            if (exception instanceof FailedProcessingException)
                                firstException = new ProcessorStateException(
                                    format("%sFailed to close state store %s", logPrefix, store.name()),
                                    exception.getCause());
                            else if (exception instanceof StreamsException)
                                firstException = exception;
                            else
                                firstException = new ProcessorStateException(
                                    format("%sFailed to close state store %s", logPrefix, store.name()), exception);
                            log.error("Failed to close state store {}: ", store.name(), firstException);
                        } else {
                            log.error("Failed to close state store {}: ", store.name(), exception);
                        }
                    }
    
                    // collect offsets for potential downgrade checkpoint
                    if (metadata.changelogPartition != null && !metadata.corrupted) {
                        allOffsets.put(metadata.changelogPartition, metadata.offset);
                    }
                }
    
                stores.clear();
            }
    
            LegacyCheckpointingStateStore.maybeDowngradeOffsets(logPrefix, upgradeFrom, stateDirectory, taskId, allOffsets);
    
            if (firstException != null) {
                throw firstException;
            }
        }
}
