public class kafka_0152 {

        @Setup
        public void init() {
            final String DEFAULT_STRING_VALUE = "NO_CACHING";
            // For v0 batches a zero starting offset is much faster but that will almost never happen.
            // For v2 batches we use starting offset = 0 as these batches are relative to the base
            // offset and measureValidation will mutate these batches between iterations
            startingOffset = messageVersion == 2 ? 0 : 42;
    
            if (bufferSupplierStr.equals(DEFAULT_STRING_VALUE)) {
                requestLocal = RequestLocal.noCaching();
            } else if (bufferSupplierStr.equals("CREATE")) {
                requestLocal = RequestLocal.withThreadConfinedCaching();
            } else {
                throw new IllegalArgumentException("Unsupported buffer supplier " + bufferSupplierStr);
            }
            singleBatchBuffer = createBatch(1);
    
            batchBuffers = new ByteBuffer[batchCount];
            for (int i = 0; i < batchCount; ++i) {
                int size = random.nextInt(maxBatchSize) + 1;
                batchBuffers[i] = createBatch(size);
            }
        }
}
