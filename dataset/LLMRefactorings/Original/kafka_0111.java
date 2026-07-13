public class kafka_0111 {

        @Override
        public TimestampedWindowStore<K, V> build() {
            WindowStore<Bytes, byte[]> store = storeSupplier.get();
            if (!(store instanceof TimestampedBytesStore)) {
                if (store.persistent()) {
                    store = new WindowToTimestampedWindowByteStoreAdapter(store);
                } else {
                    store = new InMemoryTimestampedWindowStoreMarker(store);
                }
            }
            if (storeSupplier.retainDuplicates() && enableCaching) {
                LOG.warn("Disabling caching for {} since store was configured to retain duplicates", storeSupplier.name());
                enableCaching = false;
            }
    
            return new MeteredTimestampedWindowStore<>(
                maybeWrapCaching(maybeWrapLogging(store)),
                storeSupplier.windowSize(),
                storeSupplier.metricsScope(),
                time,
                keySerde,
                valueSerde);
        }
}
