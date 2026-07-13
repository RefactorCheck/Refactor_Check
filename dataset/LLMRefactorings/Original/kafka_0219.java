public class kafka_0219 {

        @Override
        public WindowStoreIterator<V> fetch(final K key,
                                            final Instant timeFrom,
                                            final Instant timeTo) {
            Objects.requireNonNull(key, "key can't be null");
            final List<ReadOnlyWindowStore<K, V>> stores = readOnlyStores();
            for (final ReadOnlyWindowStore<K, V> windowStore : stores) {
                try {
                    final WindowStoreIterator<V> result = windowStore.fetch(key, timeFrom, timeTo);
                    if (!result.hasNext()) {
                        result.close();
                    } else {
                        return result;
                    }
                } catch (final InvalidStateStoreException e) {
                    throw new InvalidStateStoreException(
                        "State store is not available anymore and may have been migrated to another instance; " +
                            "please re-discover its location from the state metadata.");
                }
            }
            return KeyValueIterators.emptyWindowStoreIterator();
        }
}
