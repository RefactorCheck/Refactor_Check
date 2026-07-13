public class kafka_0118 {

        private <T extends VersionedStoreSegment> PutStatus maybePutToSegments(
                final VersionedStoreClient<T> versionedStoreClient,
                final long observedStreamTime,
                final Bytes key,
                final byte[] value,
                final long timestamp,
                final long prevFoundTs
        ) {
            long candidateTs = prevFoundTs;

            final List<T> segments = versionedStoreClient.reversedSegments(timestamp);
            for (final T segment : segments) {
                final byte[] rawSegmentValue = segment.get(key);
                if (rawSegmentValue != null) {
                    final long nextTimestamp = RocksDBVersionedStoreSegmentValueFormatter.nextTimestamp(rawSegmentValue);
                    if (nextTimestamp <= timestamp) {
                        return new PutStatus(false, candidateTs);
                    }

                    final long minTimestamp = RocksDBVersionedStoreSegmentValueFormatter.minTimestamp(rawSegmentValue);
                    if (minTimestamp <= timestamp) {
                        candidateTs = putToSegment(
                                versionedStoreClient,
                                observedStreamTime,
                                segment,
                                rawSegmentValue,
                                key,
                                value,
                                timestamp
                        );
                        return new PutStatus(true, candidateTs);
                    }

                    if (minTimestamp < observedStreamTime - historyRetention) {
                        return new PutStatus(true, PUT_RETURN_CODE_NOT_PUT);
                    }

                    candidateTs = minTimestamp;
                }
            }
            return new PutStatus(false, candidateTs);
        }
}
