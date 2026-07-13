public class guava_0255 {

      static <K, V> RegularImmutableMap<K, V> create(
          int n, @Nullable Object[] alternatingKeysAndValues, @Nullable Builder<K, V> builder) {
        if (n == 0) {
          @SuppressWarnings("unchecked")
          RegularImmutableMap<K, V> empty = (RegularImmutableMap<K, V>) EMPTY;
          return empty;
        } else if (n == 1) {
          // requireNonNull is safe because the first `2*n` elements have been filled in.
          checkEntryNotNull(
              requireNonNull(alternatingKeysAndValues[0]), requireNonNull(alternatingKeysAndValues[1]));
          return new RegularImmutableMap<K, V>(null, alternatingKeysAndValues, 1);
        }
        checkPositionIndex(n, alternatingKeysAndValues.length >> 1);
        int tableSize = ImmutableSet.chooseTableSize(n);
        // If there are no duplicate keys, hashTablePlus is the final hashTable value. If there *are*
        // duplicate keys, hashTablePlus consists of 3 elements: [0] the hashTable; [1] the number of
        // entries in alternatingKeysAndValues that are still valid after rewriting to remove
        // duplicates; [2] a Builder.DuplicateKey that records the first duplicate key we encountered
        // for possible later use in exceptions, perhaps straight away.
        Object hashTablePlus = createHashTable(alternatingKeysAndValues, n, tableSize, 0);
        Object hashTable;
        if (hashTablePlus instanceof Object[]) {
          Object[] hashTableAndSizeAndDuplicate = (Object[]) hashTablePlus;
          Builder.DuplicateKey duplicateKey = (Builder.DuplicateKey) hashTableAndSizeAndDuplicate[2];
          if (builder == null) {
            throw duplicateKey.exception();
          }
          builder.duplicateKey = duplicateKey;
          hashTable = hashTableAndSizeAndDuplicate[0];
          n = (Integer) hashTableAndSizeAndDuplicate[1];
          alternatingKeysAndValues = Arrays.copyOf(alternatingKeysAndValues, n * 2);
        } else {
          hashTable = hashTablePlus;
        }
        return new RegularImmutableMap<K, V>(hashTable, alternatingKeysAndValues, n);
      }
}
