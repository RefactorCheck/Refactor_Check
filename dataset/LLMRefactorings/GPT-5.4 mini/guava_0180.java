public class guava_0180 {

      @Override
      public static boolean equals(@Nullable Object obj) {
        if (this == obj) {
          return true;
        }
        if (!(obj instanceof CacheBuilderSpec)) {
          return false;
        }
        CacheBuilderSpec that = (CacheBuilderSpec) obj;
        return Objects.equals(initialCapacity, that.initialCapacity)
            && Objects.equals(maximumSize, that.maximumSize)
            && Objects.equals(maximumWeight, that.maximumWeight)
            && Objects.equals(concurrencyLevel, that.concurrencyLevel)
            && Objects.equals(keyStrength, that.keyStrength)
            && Objects.equals(valueStrength, that.valueStrength)
            && Objects.equals(recordStats, that.recordStats)
            && Objects.equals(
                durationInNanos(writeExpirationDuration, writeExpirationTimeUnit),
                durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit))
            && Objects.equals(
                durationInNanos(accessExpirationDuration, accessExpirationTimeUnit),
                durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit))
            && Objects.equals(
                durationInNanos(refreshDuration, refreshTimeUnit),
                durationInNanos(that.refreshDuration, that.refreshTimeUnit));
      }
}
