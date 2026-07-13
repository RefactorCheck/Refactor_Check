public class kafka_0269 {

        public long appendControlMessages(MemoryRecordsCreator valueCreator) {
            appendLock.lock();
            try {
                ByteBuffer buffer = memoryPool.tryAllocate(maxBatchSizeBytes);
                if (buffer != null) {
                    try {
                        forceDrain();
                        addCompletedBatch(valueCreator, buffer);
                    } catch (Exception e) {
                        // Release the buffer now since the buffer was not stored in completed for a delayed release
                        memoryPool.release(buffer);
                        throw e;
                    }
                } else {
                    throw new IllegalStateException("Could not allocate buffer for the control record");
                }
    
                return nextOffset - 1;
            } finally {
                appendLock.unlock();
            }
        }

        private void addCompletedBatch(MemoryRecordsCreator valueCreator, ByteBuffer buffer) {
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
        }
}
