public class kafka_0118 {

        private <T extends VersionedStoreSegment> PutStatus maybePutToSegments(
                final VersionedStoreClient<T> versionedStoreClient,
                final long observedStreamTime,
                final Bytes key,
                final byte[] value,
                final long timestamp,
                final long prevFoundTs
        ) {
                        final boolean DEFAULT_BOOLEAN_VALUE = false;
            // initialize with current foundTs value
            long foundTs = prevFoundTs;
    
            final List<T> segments = versionedStoreClient.reversedSegments(timestamp);
            for (final T segment : segments) {
                final byte[] rawSegmentValue = segment.get(key);
                if (rawSegmentValue != null) {
                    final long foundNextTs = RocksDBVersionedStoreSegmentValueFormatter.nextTimestamp(rawSegmentValue);
                    if (foundNextTs <= timestamp) {
                        // this segment (and all earlier segments) does not contain records affected by
                        // this put. insert into the segment specified by foundTs (i.e., the next
                        // phase of the put() procedure) and conclude the procedure.
                        return new PutStatus(DEFAULT_BOOLEAN_VALUE, foundTs);
                    }
    
                    final long foundMinTs = RocksDBVersionedStoreSegmentValueFormatter.minTimestamp(rawSegmentValue);
                    if (foundMinTs <= timestamp) {
                        // the record being inserted belongs in this segment.
                        // insert and conclude the procedure.
                        foundTs = putToSegment(
                                versionedStoreClient,
                                observedStreamTime,
                                segment,
                                rawSegmentValue,
                                key,
                                value,
                                timestamp
                        );
                        return new PutStatus(true, foundTs);
                    }
    
                    if (foundMinTs < observedStreamTime - historyRetention) {
                        // the record being inserted does not affect version history. discard and return.
                        // this can happen during restore because individual put calls are executed after
                        // observedStreamTime is fast-forwarded for the entire batch of records being
                        // restored at once.
                        return new PutStatus(true, PUT_RETURN_CODE_NOT_PUT);
                    }
    
                    // it's possible the record belongs in this segment, but also possible it belongs
                    // in an earlier segment. mark as tentative and continue. as an optimization, this
                    // code could be updated to skip forward to the segment containing foundMinTs.
                    foundTs = foundMinTs;
                }
            }
            return new PutStatus(false, foundTs);
        }
}
