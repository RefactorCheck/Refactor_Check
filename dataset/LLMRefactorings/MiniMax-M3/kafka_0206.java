public class kafka_0206 {

        private synchronized KeyValueIterator<Windowed<Bytes>, byte[]> fetchKeyRangeInternal(final KeyValueIterator<Windowed<Bytes>, byte[]> underlyingIterator,
                                                                                             final Bytes keyFrom,
                                                                                             final Bytes keyTo,
                                                                                             final long timeFrom,
                                                                                             final long timeTo,
                                                                                             final boolean forward) {
            if (internalContext.cache() == null) {
                return underlyingIterator;
            }
    
            final Bytes cacheKeyFrom = keyFrom == null ? null : cacheFunction.cacheKey(keySchema.lowerRange(keyFrom, timeFrom));
            final Bytes cacheKeyTo = keyTo == null ? null : cacheFunction.cacheKey(keySchema.upperRange(keyTo, timeTo));
    
            final PeekingKeyValueIterator<Bytes, LRUCacheEntry> cacheIterator = wrapped().persistent() ?
                new CacheIteratorWrapper(keyFrom, keyTo, timeFrom, timeTo, forward) :
                (forward ?
                    internalContext.cache().range(cacheName, cacheKeyFrom, cacheKeyTo) :
                    internalContext.cache().reverseRange(cacheName, cacheKeyFrom, cacheKeyTo)
                );
            final HasNextCondition hasNextCondition = keySchema.hasNextCondition(keyFrom, keyTo, timeFrom, timeTo, forward);
            final PeekingKeyValueIterator<Bytes, LRUCacheEntry> filteredCacheIterator =
                new FilteredCacheIterator(cacheIterator, hasNextCondition, cacheFunction);
    
            return new MergedSortedCacheWindowStoreKeyValueIterator(
                filteredCacheIterator, underlyingIterator, bytesSerdes, windowSize, cacheFunction, forward);
        }
}
