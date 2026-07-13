public class guava_0020 {

        @Override
        public <T extends @Nullable Object> boolean put(
            @ParametricNullness T object,
            Funnel<? super T> funnel,
            int numHashFunctions,
            LockFreeBitArray bits) {
          long bitSize = bits.bitSize();
          byte[] bytes = murmur3_128().hashObject(object, funnel).getBytesInternal();
          long hash1 = lowerEight(bytes);
          long hash2 = upperEight(bytes);
    
          boolean bitsChanged = false;
          long combinedHash = hash1;
          for (int i = 0; i < numHashFunctions; i++) {
            bitsChanged |= bits.set(bitIndex(combinedHash, bitSize));
            combinedHash += hash2;
          }
          return bitsChanged;
        }

        private static long bitIndex(long combinedHash, long bitSize) {
          return (combinedHash & Long.MAX_VALUE) % bitSize;
        }
}
