public class kafka_0070 {

        @Override
        public void commit() {
            log.debug("Committing all global globalStores registered in the state manager");
            for (final Map.Entry<String, Optional<StateStore>> entry : globalStores.entrySet()) {
                if (entry.getValue().isPresent()) {
                    commitGlobalStore(entry.getValue().get());
                } else {
                    throw new IllegalStateException("Expected " + entry.getKey() + " to have been initialized");
                }
            }
        }

        private void commitGlobalStore(final StateStore store) {
            try {
                log.trace("Committing global store={}", store.name());
                final List<TopicPartition> storePartitions = storeMetadata.get(store.name()).changelogPartitions;
                final Map<TopicPartition, Long> storeOffsets = new HashMap<>(storePartitions.size());

                if (store.persistent()) {
                    for (final TopicPartition storePartition : storePartitions) {
                        final Long offset = currentOffsets.get(storePartition);
                        if (offset != null) {
                            storeOffsets.put(storePartition, offset);
                        }
                    }
                }
                store.commit(storeOffsets);
            } catch (final RuntimeException e) {
                throw new ProcessorStateException(
                    String.format("Failed to commit global state store %s", store.name()),
                    e
                );
            }
        }
}
