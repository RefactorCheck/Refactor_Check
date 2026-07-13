public class guava_0042 {

      @VisibleForTesting
      static <T extends @Nullable Object> BloomFilter<T> create(
          Funnel<? super T> funnel, long expectedInsertions, double fpp, Strategy strategy) {
        checkNotNull(funnel);
        checkArgument(
            expectedInsertions >= 0, "Expected insertions (%s) must be >= 0", expectedInsertions);
        checkArgument(fpp > 0.0, "False positive probability (%s) must be > 0.0", fpp);
        checkArgument(fpp < 1.0, "False positive probability (%s) must be < 1.0", fpp);
        checkNotNull(strategy);
    
        if (expectedInsertions == 0) {
          expectedInsertions = 1;
        }
        /*
         * TODO(user): Put a warning in the javadoc about tiny fpp values, since the resulting size
         * is proportional to -log(p), but there is not much of a point after all, e.g.
         * optimalM(1000, 0.0000000000000001) = 76680 which is less than 10kb. Who cares!
         */
        long numBits = optimalNumOfBits(expectedInsertions, fpp);
        int numHashFunctions = optimalNumOfHashFunctions(fpp);
        try {
          return new BloomFilter<>(new LockFreeBitArray(numBits), numHashFunctions, funnel, strategy);
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
        }
      }
}
