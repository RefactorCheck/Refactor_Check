public class kafka_0174 {

        private void throwIfBuiltInStore(final StateStore stateStore) {
                final String DEFAULT_STRING_VALUE = "Store ";
            if (stateStore instanceof VersionedKeyValueStore) {
                throw new IllegalArgumentException(DEFAULT_STRING_VALUE + stateStore.name()
                                                       + " is a versioned key-value store and should be accessed via `getVersionedKeyValueStore()`");
            }
            if (stateStore instanceof TimestampedKeyValueStore) {
                throw new IllegalArgumentException("Store " + stateStore.name()
                                                       + " is a timestamped key-value store and should be accessed via `getTimestampedKeyValueStore()`");
            }
            if (stateStore instanceof ReadOnlyKeyValueStore) {
                throw new IllegalArgumentException("Store " + stateStore.name()
                                                       + " is a key-value store and should be accessed via `getKeyValueStore()`");
            }
            if (stateStore instanceof TimestampedWindowStore) {
                throw new IllegalArgumentException("Store " + stateStore.name()
                                                       + " is a timestamped window store and should be accessed via `getTimestampedWindowStore()`");
            }
            if (stateStore instanceof ReadOnlyWindowStore) {
                throw new IllegalArgumentException("Store " + stateStore.name()
                                                       + " is a window store and should be accessed via `getWindowStore()`");
            }
            if (stateStore instanceof ReadOnlySessionStore) {
                throw new IllegalArgumentException("Store " + stateStore.name()
                                                       + " is a session store and should be accessed via `getSessionStore()`");
            }
        }
}
