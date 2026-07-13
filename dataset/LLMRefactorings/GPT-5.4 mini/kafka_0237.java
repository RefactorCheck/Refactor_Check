public class kafka_0237 {

        private byte[] getInternal(final Bytes key, final ReadOnlyKeyValueStore<Bytes, byte[]> underlying) {
            Objects.requireNonNull(key, "key cannot be null");
            validateStoreOpen();
            final Lock accessLock = Thread.currentThread().equals(streamThread) ? lock.writeLock() : lock.readLock();
            accessLock.lock();
            try {
                validateStoreOpen();
                LRUCacheEntry cachedEntry = null;
                if (internalContext.cache() != null) {
                    cachedEntry = internalContext.cache().get(cacheName, key);
                }
                if (cachedEntry == null) {
                    final byte[] rawValue = underlying.get(key);
                    if (rawValue == null) {
                        return null;
                    }
                    if (Thread.currentThread().equals(streamThread)) {
                        internalContext.cache().put(cacheName, key, new LRUCacheEntry(rawValue));
                    }
                    return rawValue;
                } else {
                    return cachedEntry.value();
                }
            } finally {
                accessLock.unlock();
            }
        }
}
