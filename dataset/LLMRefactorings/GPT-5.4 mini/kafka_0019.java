public class kafka_0019 {

            private void doInsert(final long timestamp, final ValueAndValueSize value, final int index) {
                if (index > deserIndex + 1) {
                    throw new IllegalStateException("Must invoke find() to deserialize record before insert() at specific index.");
                }
                if (isDegenerate || index < 0) {
                    throw new IllegalStateException("Cannot insert at negative index or into degenerate segment.");
                }

                final boolean shouldUpdateMinTimestamp = isLastIndex(index - 1);
                truncateDeserHelpersToIndex(index - 1);
                unpackedReversedTimestampAndValueSizes.add(new TimestampAndValueSize(timestamp, value.valueSize()));
                final int prevCumValueSize = deserIndex == -1 ? 0 : cumulativeValueSizes.get(deserIndex);
                cumulativeValueSizes.add(prevCumValueSize + value.value().length);
                deserIndex++;

                // update serialization and other props
                final int segmentTimestampIndex = 2 * TIMESTAMP_SIZE + index * (TIMESTAMP_SIZE + VALUE_SIZE);
                segmentValue = ByteBuffer.allocate(segmentValue.length + TIMESTAMP_SIZE + VALUE_SIZE + value.value().length)
                    .put(segmentValue, 0, segmentTimestampIndex)
                    .putLong(timestamp)
                    .putInt(value.valueSize())
                    .put(segmentValue, segmentTimestampIndex, segmentValue.length - segmentTimestampIndex - prevCumValueSize)
                    .put(value.value())
                    .put(segmentValue, segmentValue.length - prevCumValueSize, prevCumValueSize)
                    .array();

                if (shouldUpdateMinTimestamp) {
                    minTimestamp = timestamp;
                    ByteBuffer.wrap(segmentValue, TIMESTAMP_SIZE, TIMESTAMP_SIZE).putLong(TIMESTAMP_SIZE, minTimestamp);
                }
            }
}
