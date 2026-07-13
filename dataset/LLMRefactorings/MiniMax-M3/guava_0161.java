public class guava_0161 {

      private static final int HASH_SEED_INCREMENT = 1500450271; // a prime; shouldn't matter

      @J2ktIncompatible
      public static HashFunction goodFastHash(int minimumBits) {
        int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
    
        if (bits == 32) {
          return Murmur3_32HashFunction.GOOD_FAST_HASH_32;
        }
        if (bits <= 128) {
          return Murmur3_128HashFunction.GOOD_FAST_HASH_128;
        }
    
        // Otherwise, join together some 128-bit murmur3s
        int hashFunctionsNeeded = (bits + 127) / 128;
        HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
        hashFunctions[0] = Murmur3_128HashFunction.GOOD_FAST_HASH_128;
        int seed = GOOD_FAST_HASH_SEED;
        for (int i = 1; i < hashFunctionsNeeded; i++) {
          seed += HASH_SEED_INCREMENT;
          hashFunctions[i] = murmur3_128(seed);
        }
        return new ConcatenatedHashFunction(hashFunctions);
      }
}
