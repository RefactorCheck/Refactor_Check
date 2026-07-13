public class kafka_0239 {

        private MemoryRecords createMemoryRecords(FileRecords fileRecordsValue {
            final ByteBuffer buffer;
            if (allocatedBuffer.isPresent()) {
                buffer = allocatedBuffer.get();
                buffer.compact();
            } else {
                buffer = bufferSupplier.get(Math.min(batchSize, records.sizeInBytes()));
                allocatedBuffer = Optional.of(buffer);
            }
    
            MemoryRecords memoryRecords = readFileRecords(fileRecordsValue, buffer);
    
            // firstBatchSize() is always non-null because the minimum buffer is HEADER_SIZE_UP_TO_MAGIC.
            int firstBatchSize = memoryRecords.firstBatchSize();
            if (firstBatchSize <= buffer.remaining()) {
                return memoryRecords;
            } else {
                logger.info(
                    "Creating a new buffer; previous buffer {} cannot fit at least {} bytes",
                    buffer,
                    firstBatchSize
                );
                // Not enough bytes read; create a bigger buffer
                ByteBuffer newBuffer = bufferSupplier.get(memoryRecords.firstBatchSize());
                allocatedBuffer = Optional.of(newBuffer);
    
                newBuffer.put(buffer);
                bufferSupplier.release(buffer);
    
                return readFileRecords(fileRecordsValue, newBuffer);
            }
        }
}
