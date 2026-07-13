public class kafka_0174 {

        private void throwIfBuiltInStore(final StateStore stateStore) {
            if (stateStore instanceof VersionedKeyValueStore) {
                throwBuiltInStoreException(stateStore, "a versioned key-value store", "getVersionedKeyValueStore()");
            }
            if (stateStore instanceof TimestampedKeyValueStore) {
                throwBuiltInStoreException(stateStore, "a timestamped key-value store", "getTimestampedKeyValueStore()");
            }
            if (stateStore instanceof ReadOnlyKeyValueStore) {
                throwBuiltInStoreException(stateStore, "a key-value store", "getKeyValueStore()");
            }
            if (stateStore instanceof TimestampedWindowStore) {
                throwBuiltInStoreException(stateStore, "a timestamped window store", "getTimestampedWindowStore()");
            }
            if (stateStore instanceof ReadOnlyWindowStore) {
                throwBuiltInStoreException(stateStore, "a window store", "getWindowStore()");
            }
            if (stateStore instanceof ReadOnlySessionStore) {
                throwBuiltInStoreException(stateStore, "a session store", "getSessionStore()");
            }
        }

        private void throwBuiltInStoreException(final StateStore stateStore, final String storeType, final String accessorMethod) {
            throw new IllegalArgumentException("Store " + stateStore.name()
                                                   + " is " + storeType
                                                   + " and should be accessed via `" + accessorMethod + "`");
        }
}
