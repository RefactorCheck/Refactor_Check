public class kafka_0269 {

        private static final String CONTROL_RECORD_BUFFER_ALLOCATION_ERROR =
            "Could not allocate buffer for the control record";

        public long appendControlMessages(MemoryRecordsCreator valueCreator) {
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
                    throw new IllegalStateException(CONTROL_RECORD_BUFFER_ALLOCATION_ERROR);
                }

                return nextOffset - 1;
            } finally {
                appendLock.unlock();
            }
        }
}
