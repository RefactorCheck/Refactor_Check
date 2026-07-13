public class kafka_0233 {

        @Override
        public KeyValueIterator<Windowed<K>, V> findSessions(final K key,
                                                             final long earliestSessionEndTime,
                                                             final long latestSessionStartTime) {
            Objects.requireNonNull(key, "key can't be null");
            final List<ReadOnlySessionStore<K, V>> stores = readOnlyStores();
            for (final ReadOnlySessionStore<K, V> store : stores) {
                final KeyValueIterator<Windowed<K>, V> result =
                    findSessionsInStore(store, key, earliestSessionEndTime, latestSessionStartTime);
                if (result != null) {
                    return result;
                }
            }
            return KeyValueIterators.emptyIterator();
        }

        private KeyValueIterator<Windowed<K>, V> findSessionsInStore(
                final ReadOnlySessionStore<K, V> store,
                final K key,
                final long earliestSessionEndTime,
                final long latestSessionStartTime) {
            try {
                final KeyValueIterator<Windowed<K>, V> result =
                    store.findSessions(key, earliestSessionEndTime, latestSessionStartTime);
                if (!result.hasNext()) {
                    result.close();
                    return null;
                }
                return result;
            } catch (final InvalidStateStoreException ise) {
                throw new InvalidStateStoreException(
                    "State store  [" + storeName + "] is not available anymore" +
                        " and may have been migrated to another instance; " +
                        "please re-discover its location from the state metadata.",
                    ise
                );
            }
        }
}
