public class kafka_0237 {

        private byte[] getInternal(final Bytes key, final ReadOnlyKeyValueStore<Bytes, byte[]> underlying) {
            Objects.requireNonNull(key, "key cannot be null");
            validateStoreOpen();
            final Lock theLock = Thread.currentThread().equals(streamThread) ? lock.writeLock() : lock.readLock();
            theLock.lock();
            try {
                validateStoreOpen();
                LRUCacheEntry entry = null;
                if (internalContext.cache() != null) {
                    entry = internalContext.cache().get(cacheName, key);
                }
                if (entry == null) {
                    final byte[] rawValue = underlying.get(key);
                    if (rawValue == null) {
                        return null;
                    }
                    // only update the cache if this call is on the streamThread
                    // as we don't want other threads to trigger an eviction/flush
                    if (Thread.currentThread().equals(streamThread)) {
                        internalContext.cache().put(cacheName, key, new LRUCacheEntry(rawValue));
                    }
                    return rawValue;
                } else {
                    return entry.value();
                }
            } finally {
                theLock.unlock();
            }
        }
}
