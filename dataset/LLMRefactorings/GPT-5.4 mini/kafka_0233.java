public class kafka_0233 {

        @Override
        public KeyValueIterator<Windowed<K>, V> findSessions(final K key,
                                                             final long earliestSessionEndTime,
                                                             final long latestSessionStartTime) {
            Objects.requireNonNull(key, "key can't be null");
            for (final ReadOnlySessionStore<K, V> store : readOnlyStores()) {
                try {
                    final KeyValueIterator<Windowed<K>, V> result =
                        store.findSessions(key, earliestSessionEndTime, latestSessionStartTime);

                    if (!result.hasNext()) {
                        result.close();
                    } else {
                        return result;
                    }
                } catch (final InvalidStateStoreException ise) {
                    throw new InvalidStateStoreException(
                        "State store  [" + storeName + "] is not available anymore" +
                            " and may have been migrated to another instance; " +
                            "please re-discover its location from the state metadata.",
                        ise
                    );
                }
            }
            return KeyValueIterators.emptyIterator();
        }
}
