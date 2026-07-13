public class kafka_0168 {

        @Override
        public void put(final Bytes rawBaseKey,
                        final byte[] value) {
            final long timestamp = baseKeySchema.segmentTimestamp(rawBaseKey);
            observedStreamTime = Math.max(observedStreamTime, timestamp);
            final long segmentId = segments.segmentId(timestamp);
            final S segment = segments.getOrCreateSegmentIfLive(segmentId, internalProcessorContext, observedStreamTime);
    
            if (segment == null) {
                expiredRecordSensor.record(1.0d, internalProcessorContext.currentSystemTimeMs());
                LOG.warn("Skipping record for expired segment.");
            } else {
                synchronized (position) {
                    StoreQueryUtils.updatePosition(position, internalProcessorContext);
    
                    // Put to index first so that if put to base failed, when we iterate index, we will
                    // find no base value. If put to base first but putting to index fails, when we iterate
                    // index, we can't find the key but if we iterate over base store, we can find the key
                    // which lead to inconsistency.
                    if (hasIndex()) {
                        final KeyValue<Bytes, byte[]> indexKeyValue = getIndexKeyValue(rawBaseKey, value);
                        segment.put(indexKeyValue.key, indexKeyValue.value);
                    }
                    segment.put(rawBaseKey, value);
                }
            }
        }
}
