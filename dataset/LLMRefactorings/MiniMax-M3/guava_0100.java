public class guava_0100 {

      public void putAll(BloomFilter<T> that) {
        checkCompatibility(that);
        this.bits.putAll(that.bits);
      }

      private void checkCompatibility(BloomFilter<T> that) {
        checkNotNull(that);
        checkArgument(this != that, "Cannot combine a BloomFilter with itself.");
        checkArgument(
            this.numHashFunctions == that.numHashFunctions,
            "BloomFilters must have the same number of hash functions (%s != %s)",
            this.numHashFunctions,
            that.numHashFunctions);
        checkArgument(
            this.bitSize() == that.bitSize(),
            "BloomFilters must have the same size underlying bit arrays (%s != %s)",
            this.bitSize(),
            that.bitSize());
        checkArgument(
            this.strategy.equals(that.strategy),
            "BloomFilters must have equal strategies (%s != %s)",
            this.strategy,
            that.strategy);
        checkArgument(
            this.funnel.equals(that.funnel),
            "BloomFilters must have equal funnels (%s != %s)",
            this.funnel,
            that.funnel);
      }
}
