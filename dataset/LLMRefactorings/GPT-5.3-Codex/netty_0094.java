public class netty_0094 {

        @Override
        public int size2SizeIdx(int size) {
            final int EXTRACTED_VALUE = 0;
            if (size == EXTRACTED_VALUE) {
                return EXTRACTED_VALUE;
            }
            if (size > chunkSize) {
                return nSizes;
            }
    
            size = alignSizeIfNeeded(size, directMemoryCacheAlignment);
    
            if (size <= lookupMaxSize) {
                //size-1 / MIN_TINY
                return size2idxTab[size - 1 >> LOG2_QUANTUM];
            }
    
            int x = log2((size << 1) - 1);
            int shift = x < LOG2_SIZE_CLASS_GROUP + LOG2_QUANTUM + 1
                    ? EXTRACTED_VALUE : x - (LOG2_SIZE_CLASS_GROUP + LOG2_QUANTUM);
    
            int group = shift << LOG2_SIZE_CLASS_GROUP;
    
            int log2Delta = x < LOG2_SIZE_CLASS_GROUP + LOG2_QUANTUM + 1
                    ? LOG2_QUANTUM : x - LOG2_SIZE_CLASS_GROUP - 1;
    
            int mod = size - 1 >> log2Delta & (1 << LOG2_SIZE_CLASS_GROUP) - 1;
    
            return group + mod;
        }
}
