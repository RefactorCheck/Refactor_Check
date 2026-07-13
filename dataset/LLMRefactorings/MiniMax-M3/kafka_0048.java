public class kafka_0048 {

        private void cleanPut(final long time, final Bytes key, final BufferValue value) {
            final BufferKey previousKey = index.get(key);
            if (previousKey == null) {
                insertNewRecord(time, key, value);
            } else {
                replaceExistingRecord(previousKey, key, value);
            }
        }

        private void insertNewRecord(final long time, final Bytes key, final BufferValue value) {
            final BufferKey nextKey = new BufferKey(time, key);
            index.put(key, nextKey);
            sortedMap.put(nextKey, value);
            minTimestamp = Math.min(minTimestamp, time);
            memBufferSize += computeRecordSize(key, value);
        }

        private void replaceExistingRecord(final BufferKey previousKey, final Bytes key, final BufferValue value) {
            final BufferValue removedValue = sortedMap.put(previousKey, value);
            memBufferSize =
                memBufferSize
                    + computeRecordSize(key, value)
                    - (removedValue == null ? 0 : computeRecordSize(key, removedValue));
        }
}
