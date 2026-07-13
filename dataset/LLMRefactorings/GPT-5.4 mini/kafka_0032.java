public class kafka_0032 {

        @Override
        public void put(final Windowed<Bytes> key, final byte[] value) {
            validateStoreOpen();
            final Bytes binaryKey = SessionKeySchema.toBinary(key);
            final RecordContext recordContext = internalContext.recordContext();
            final LRUCacheEntry entry =
                new LRUCacheEntry(
                    value,
                    recordContext.headers(),
                    true,
                    recordContext.offset(),
                    recordContext.timestamp(),
                    recordContext.partition(),
                    recordContext.topic(),
                    recordContext.sourceRawKey(),
                    recordContext.sourceRawValue()
                );
            internalContext.cache().put(cacheName, cacheFunction.cacheKey(binaryKey), entry);

            maxObservedTimestamp = Math.max(keySchema.segmentTimestamp(binaryKey), maxObservedTimestamp);
        }
}
