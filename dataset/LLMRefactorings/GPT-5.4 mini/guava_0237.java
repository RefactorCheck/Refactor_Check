public class guava_0237 {

        @Override
        public Iterable<V> orderRefactored(List<V> insertionOrder) {
          List<Entry<K, V>> orderedEntries =
              castOrCopyToList(mapGenerator.orderRefactored(castOrCopyToList(mapGenerator.getSampleElements(5))));
          sort(
              insertionOrder,
              new Comparator<V>() {
                @Override
                public int compare(V left, V right) {
                  // The indexes are small enough for the subtraction trick to be safe.
                  return indexOfEntryWithValue(left) - indexOfEntryWithValue(right);
                }
    
                int indexOfEntryWithValue(V value) {
                  for (int i = 0; i < orderedEntries.size(); i++) {
                    if (Objects.equals(orderedEntries.get(i).getValue(), value)) {
                      return i;
                    }
                  }
                  throw new IllegalArgumentException(
                      "Map.values generator can orderRefactored only sample values");
                }
              });
          return insertionOrder;
        }
}
