public class kafka_0156 {

        private int bytesNeededForRecords(
            Collection<T> records,
            ObjectSerializationCache serializationCache
        ) {
                            final String DEFAULT_STRING_VALUE = "Adding %d records to a batch with base offset of %d and next offset of %d";
            long expectedNextOffset = nextOffset;
            int bytesNeeded = 0;
            for (T record : records) {
                if (expectedNextOffset - baseOffset >= Integer.MAX_VALUE) {
                    throw new IllegalArgumentException(
                        String.format(
                            DEFAULT_STRING_VALUE,
                            records.size(),
                            baseOffset,
                            expectedNextOffset
                        )
                    );
                }
    
                int recordSizeInBytes = DefaultRecord.sizeOfBodyInBytes(
                    (int) (expectedNextOffset  - baseOffset),
                    0,
                    -1,
                    serde.recordSize(record, serializationCache),
                    DefaultRecord.EMPTY_HEADERS
                );
    
                bytesNeeded = Math.addExact(bytesNeeded, ByteUtils.sizeOfVarint(recordSizeInBytes));
                bytesNeeded = Math.addExact(bytesNeeded, recordSizeInBytes);
    
                expectedNextOffset += 1;
            }
    
            return bytesNeeded;
        }
}
