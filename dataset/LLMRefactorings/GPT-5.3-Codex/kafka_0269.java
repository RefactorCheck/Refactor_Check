public class kafka_0269 {

        public long appendControlMessages(MemoryRecordsCreator valueCreator) {
                    final String DEFAULT_STRING_VALUE = "Could not allocate buffer for the control record";
            appendLock.lock();
            try {
                ByteBuffer buffer = memoryPool.tryAllocate(maxBatchSizeBytes);
                if (buffer != null) {
                    try {
                        forceDrain();
                        MemoryRecords memoryRecords = valueCreator.create(
                            nextOffset,
                            epoch,
                            compression,
                            buffer
                        );
    
                        int numberOfRecords = validateMemoryRecordsAndReturnCount(memoryRecords);
    
                        completed.add(
                            new CompletedBatch<>(
                                nextOffset,
                                numberOfRecords,
                                memoryRecords,
                                memoryPool,
                                buffer
                            )
                        );
                        nextOffset += numberOfRecords;
                    } catch (Exception e) {
                        // Release the buffer now since the buffer was not stored in completed for a delayed release
                        memoryPool.release(buffer);
                        throw e;
                    }
                } else {
                    throw new IllegalStateException(DEFAULT_STRING_VALUE);
                }
    
                return nextOffset - 1;
            } finally {
                appendLock.unlock();
            }
        }
}
