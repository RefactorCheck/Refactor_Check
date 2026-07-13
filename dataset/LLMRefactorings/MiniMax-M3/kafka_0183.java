public class kafka_0183 {

    private Collection<StreamsMetadata> filterMetadataByStore(final Collection<StreamsMetadata> metadataCollection, final String storeName) {
        final ArrayList<StreamsMetadata> results = new ArrayList<>();
        for (final StreamsMetadata metadata : metadataCollection) {
            if (metadata.stateStoreNames().contains(storeName) || metadata.standbyStateStoreNames().contains(storeName)) {
                results.add(metadata);
            }
        }
        return results;
    }

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

        return filterMetadataByStore(allMetadata, storeName);
    }
}
