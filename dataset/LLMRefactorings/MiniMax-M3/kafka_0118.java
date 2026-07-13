public class kafka_0118 {

        private <T extends VersionedStoreSegment> PutStatus maybePutToSegments(
                final VersionedStoreClient<T> versionedStoreClient,
                final long observedStreamTime,
                final Bytes key,
                final byte[] value,
                final long timestamp,
                final long prevFoundTs
        ) {
            long foundTs = prevFoundTs;

            final List<T> segments = versionedStoreClient.reversedSegments(timestamp);
            for (final T segment : segments) {
                final byte[] rawSegmentValue = segment.get(key);
                if (rawSegmentValue != null) {
                    final long foundMinTs = RocksDBVersionedStoreSegmentValueFormatter.minTimestamp(rawSegmentValue);
                    final PutStatus result = processSegmentValue(
                            versionedStoreClient, observedStreamTime, segment, rawSegmentValue, foundMinTs,
                            key, value, timestamp, foundTs);
                    if (result != null) {
                        return result;
                    }
                    foundTs = foundMinTs;
                }
            }
            return new PutStatus(false, foundTs);
        }

        private <T extends VersionedStoreSegment> PutStatus processSegmentValue(
                final VersionedStoreClient<T> versionedStoreClient,
                final long observedStreamTime,
                final T segment,
                final byte[] rawSegmentValue,
                final long foundMinTs,
                final Bytes key,
                final byte[] value,
                final long timestamp,
                final long foundTs
        ) {
            final long foundNextTs = RocksDBVersionedStoreSegmentValueFormatter.nextTimestamp(rawSegmentValue);
            if (foundNextTs <= timestamp) {
                return new PutStatus(false, foundTs);
            }

            if (foundMinTs <= timestamp) {
                final long newFoundTs = putToSegment(
                        versionedStoreClient,
                        observedStreamTime,
                        segment,
                        rawSegmentValue,
                        key,
                        value,
                        timestamp
                );
                return new PutStatus(true, newFoundTs);
            }

            if (foundMinTs < observedStreamTime - historyRetention) {
                return new PutStatus(true, PUT_RETURN_CODE_NOT_PUT);
            }

            return null;
        }
}
