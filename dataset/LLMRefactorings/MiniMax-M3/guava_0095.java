public class guava_0095 {

      @CanIgnoreReturnValue
      @Override
      public final int add(@ParametricNullness E element, int occurrences) {
        if (occurrences == 0) {
          return count(element);
        }
        checkArgument(occurrences > 0, "occurrences cannot be negative: %s", occurrences);
        int entryIndex = backingMap.indexOf(element);
        if (entryIndex == -1) {
          backingMap.put(element, occurrences);
          size += occurrences;
          return 0;
        }
        int oldCount = backingMap.getValue(entryIndex);
        long newCount = calculateNewCount(oldCount, occurrences);
        checkArgument(newCount <= Integer.MAX_VALUE, "too many occurrences: %s", newCount);
        backingMap.setValue(entryIndex, (int) newCount);
        size += occurrences;
        return oldCount;
      }

      private static long calculateNewCount(int oldCount, int occurrences) {
        return (long) oldCount + (long) occurrences;
      }
}
