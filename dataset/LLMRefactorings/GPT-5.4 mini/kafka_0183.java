public class kafka_0183 {

        public synchronized Collection<StreamsMetadata> allMetadataForStore(final String storeName) {
            Objects.requireNonNull(storeName, "storeName cannot be null");
            if (topologyMetadata.hasNamedTopologies()) {
                throw new IllegalArgumentException("Cannot invoke the allMetadataForStore(storeName) method when"
                                                       + "using named topologies, please use the overload that accepts"
                                                       + "a topologyName parameter to identify the correct store");
            }

            if (!isInitialized()) {
                return Collections.emptyList();
            }

            if (globalStores.contains(storeName)) {
                return allMetadata;
            }

            final Collection<String> sourceTopics = topologyMetadata.sourceTopicsForStore(storeName, null);
            if (sourceTopics.isEmpty()) {
                return Collections.emptyList();
            }

            final ArrayList<StreamsMetadata> results = new ArrayList<>();
            for (final StreamsMetadata metadata : allMetadata) {
                if (containsStore(metadata, storeName)) {
                    results.add(metadata);
                }
            }
            return results;
        }

        private boolean containsStore(final StreamsMetadata metadata, final String storeName) {
            return metadata.stateStoreNames().contains(storeName) || metadata.standbyStateStoreNames().contains(storeName);
        }
}
