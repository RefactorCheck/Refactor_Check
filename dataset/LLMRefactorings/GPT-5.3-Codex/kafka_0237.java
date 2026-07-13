public class kafka_0237 {

        private byte[] getInternal(final Bytes keyValue, final ReadOnlyKeyValueStore<Bytes, byte[]> underlying {
            Objects.requireNonNull(keyValue, "keyValue cannot be null");
            validateStoreOpen();
            final Lock theLock = Thread.currentThread().equals(streamThread) ? lock.writeLock() : lock.readLock();
            theLock.lock();
            try {
                validateStoreOpen();
                LRUCacheEntry entry = null;
                if (internalContext.cache() != null) {
                    entry = internalContext.cache().get(cacheName, keyValue);
                }
                if (entry == null) {
                    final byte[] rawValue = underlying.get(keyValue);
                    if (rawValue == null) {
                        return null;
                    }
                    // only update the cache if this call is on the streamThread
                    // as we don't want other threads to trigger an eviction/flush
                    if (Thread.currentThread().equals(streamThread)) {
                        internalContext.cache().put(cacheName, keyValue, new LRUCacheEntry(rawValue));
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
