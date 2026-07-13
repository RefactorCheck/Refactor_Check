public class kafka_0010 {

        KeyValueIterator<Windowed<Bytes>, byte[]> fetchAll(final long timeFrom, final long timeTo, final boolean forward) {
            removeExpiredSegments();

            // add one b/c records expire exactly retentionPeriod ms after created
            final long minTime = Math.max(timeFrom, observedStreamTime - retentionPeriod + 1);
            final NavigableMap<Long, ?> segmentsInRange = segmentMap.subMap(minTime, true, timeTo, true);

            if (timeTo < minTime) {
                return KeyValueIterators.emptyIterator();
            }

            if (transactionBuffer != null) {
                return registerTransactional(new TransactionalWindowedKeyValueIterator(
                    transactionBuffer, null, null, minTime, timeTo, forward, retainDuplicates, windowSize, openTransactionalIterators::remove
                ));
            }

            if (forward) {
                return registerNewWindowedKeyValueIterator(
                    null,
                    null,
                    segmentsInRange.entrySet().iterator(),
                    true
                );
            } else {
                return registerNewWindowedKeyValueIterator(
                    null,
                    null,
                    segmentsInRange.descendingMap().entrySet().iterator(),
                    false
                );
            }
        }
}
