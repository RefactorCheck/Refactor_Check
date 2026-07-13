public class guava_0180 {

      @Override
      public boolean equals(@Nullable Object obj) {
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
            && durationsEqual(writeExpirationDuration, writeExpirationTimeUnit,
                that.writeExpirationDuration, that.writeExpirationTimeUnit)
            && durationsEqual(accessExpirationDuration, accessExpirationTimeUnit,
                that.accessExpirationDuration, that.accessExpirationTimeUnit)
            && durationsEqual(refreshDuration, refreshTimeUnit,
                that.refreshDuration, that.refreshTimeUnit);
      }

      private static boolean durationsEqual(long duration, TimeUnit unit,
          long otherDuration, TimeUnit otherUnit) {
        return Objects.equals(
            durationInNanos(duration, unit),
            durationInNanos(otherDuration, otherUnit));
      }
}
