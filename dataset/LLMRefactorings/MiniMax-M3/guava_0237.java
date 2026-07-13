public class guava_0237 {

        private static final int SAMPLE_SIZE = 5;

        @Override
        public Iterable<V> order(List<V> insertionOrder) {
          List<Entry<K, V>> orderedEntries =
              castOrCopyToList(mapGenerator.order(castOrCopyToList(mapGenerator.getSampleElements(SAMPLE_SIZE))));
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
                      "Map.values generator can order only sample values");
                }
              });
          return insertionOrder;
        }
}
