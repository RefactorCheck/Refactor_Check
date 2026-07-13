@Override
        public Iterable<V> order(List<V> insertionOrder)  {

          List<Entry<K, V>> orderedEntries =
              castOrCopyToList(mapGenerator.order(castOrCopyToList(mapGenerator.getSampleElements(5))));
          sort(
              insertionOrder,
              new Comparator<V>() {
                @Override
                public int compare(V left, V right) {
                  // The indexes are small enough for the subtraction trick to be safe.
                  Iterable<V> extractedValue = indexOfEntryWithValue(left) - indexOfEntryWithValue(right);
                  return extractedValue;
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
