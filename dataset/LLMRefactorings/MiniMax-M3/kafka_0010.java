public class kafka_0010 {
    KeyValueIterator<Windowed<Bytes>, byte[]> fetchAll(final long timeFrom, final long timeTo, final boolean forward) {
        removeExpiredSegments();
        
        // add one b/c records expire exactly retentionPeriod ms after created
        final long minTime = Math.max(timeFrom, observedStreamTime - retentionPeriod + 1);
        
        if (timeTo < minTime) {
            return KeyValueIterators.emptyIterator();
        }
        
        if (transactionBuffer != null) {
            return registerTransactional(new TransactionalWindowedKeyValueIterator(
                transactionBuffer, null, null, minTime, timeTo, forward, retainDuplicates, windowSize, openTransactionalIterators::remove
            ));
        }
        
        return createIterator(minTime, timeTo, forward);
    }
    
    private KeyValueIterator<Windowed<Bytes>, byte[]> createIterator(final long minTime, final long timeTo, final boolean forward) {
        if (forward) {
            return registerNewWindowedKeyValueIterator(
                null,
                null,
                segmentMap.subMap(minTime, true, timeTo, true).entrySet().iterator(),
                true
            );
        } else {
            return registerNewWindowedKeyValueIterator(
                null,
                null,
                segmentMap.subMap(minTime, true, timeTo, true).descendingMap().entrySet().iterator(),
                false
            );
        }
    }
}
