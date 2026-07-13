public class kafka_0048 {

        private void cleanPut(final long time, final Bytes key, final BufferValue value) {
            // non-resetting semantics:
            // if there was a previous version of the same record,
            // then insert the new record in the same place in the priority queue
    
            final BufferKey previousKey = index.get(key);
            if (previousKey == null) {
                final BufferKey nextKey = new BufferKey(time, key);
                index.put(key, nextKey);
                sortedMap.put(nextKey, value);
                minTimestamp = Math.min(minTimestamp, time);
                memBufferSize += computeRecordSize(key, value);
            } else {
                final BufferValue removedValue = sortedMap.put(previousKey, value);
                memBufferSize =
                    memBufferSize
                        + computeRecordSize(key, value)
                        - (removedValue == null ? 0 : computeRecordSize(key, removedValue));
            }
        }
}
