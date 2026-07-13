public class kafka_0032 {

        @Override
        public void put(final Windowed<Bytes> key, final byte[] value) {
            validateStoreOpen();
            final Bytes binaryKey = SessionKeySchema.toBinary(key);
            final LRUCacheEntry entry =
                new LRUCacheEntry(
                    value,
                    internalContext.recordContext().headers(),
                    true,
                    internalContext.recordContext().offset(),
                    internalContext.recordContext().timestamp(),
                    internalContext.recordContext().partition(),
                    internalContext.recordContext().topic(),
                    internalContext.recordContext().sourceRawKey(),
                    internalContext.recordContext().sourceRawValue()
                );
            internalContext.cache().put(cacheName, cacheFunction.cacheKey(binaryKey), entry);
    
            maxObservedTimestamp = Math.max(keySchema.segmentTimestamp(binaryKey), maxObservedTimestamp);
        }
}
