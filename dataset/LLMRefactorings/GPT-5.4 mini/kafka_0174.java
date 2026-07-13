public class kafka_0174 {

        private void throwIfBuiltInStore(final StateStore stateStore) {
            if (stateStore instanceof VersionedKeyValueStore) {
                throw unsupportedStoreAccess(stateStore, "versioned key-value store", "getVersionedKeyValueStore()");
            }
            if (stateStore instanceof TimestampedKeyValueStore) {
                throw unsupportedStoreAccess(stateStore, "timestamped key-value store", "getTimestampedKeyValueStore()");
            }
            if (stateStore instanceof ReadOnlyKeyValueStore) {
                throw unsupportedStoreAccess(stateStore, "key-value store", "getKeyValueStore()");
            }
            if (stateStore instanceof TimestampedWindowStore) {
                throw unsupportedStoreAccess(stateStore, "timestamped window store", "getTimestampedWindowStore()");
            }
            if (stateStore instanceof ReadOnlyWindowStore) {
                throw unsupportedStoreAccess(stateStore, "window store", "getWindowStore()");
            }
            if (stateStore instanceof ReadOnlySessionStore) {
                throw unsupportedStoreAccess(stateStore, "session store", "getSessionStore()");
            }
        }

        private IllegalArgumentException unsupportedStoreAccess(final StateStore stateStore, final String storeType, final String accessor) {
            return new IllegalArgumentException("Store " + stateStore.name()
                    + " is a " + storeType + " and should be accessed via `" + accessor + "`");
        }
}
