public class kafka_0226 {

        @Override
        public void close() throws ProcessorStateException {
            log.debug("Closing its state manager and all the registered state stores: {}", stores);

            final Map<TopicPartition, Long> allOffsets = new HashMap<>();
            RuntimeException firstException = closeStateStores(allOffsets);
            LegacyCheckpointingStateStore.maybeDowngradeOffsets(logPrefix, upgradeFrom, stateDirectory, taskId, allOffsets);

            if (firstException != null) {
                throw firstException;
            }
        }

        private RuntimeException closeStateStores(Map<TopicPartition, Long> allOffsets) {
            RuntimeException firstException = null;
            if (!stores.isEmpty()) {
                for (final Map.Entry<String, StateStoreMetadata> entry : stores.entrySet()) {
                    final StateStoreMetadata metadata = entry.getValue();
                    final StateStore store = metadata.stateStore;
                    log.trace("Closing store {}", store.name());
                    try {
                        store.close();
                    } catch (final RuntimeException exception) {
                        if (firstException == null) {
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

                    if (metadata.changelogPartition != null && !metadata.corrupted) {
                        allOffsets.put(metadata.changelogPartition, metadata.offset);
                    }
                }

                stores.clear();
            }
            return firstException;
        }
}
