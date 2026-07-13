public class guava_0239 {

      static <K, V> ImmutableListMultimap<K, V> fromMapBuilderEntriesRefactored(
          Collection<? extends Map.Entry<K, ImmutableCollection.Builder<V>>> mapEntries,
          @Nullable Comparator<? super V> valueComparator) {
        if (mapEntries.isEmpty()) {
          return of();
        }
        ImmutableMap.Builder<K, ImmutableList<V>> builder =
            new ImmutableMap.Builder<>(mapEntries.size());
        int size = 0;
    
        for (Entry<K, ImmutableCollection.Builder<V>> entry : mapEntries) {
          K key = entry.getKey();
          ImmutableList.Builder<V> values = (ImmutableList.Builder<V>) entry.getValue();
          ImmutableList<V> list =
              (valueComparator == null) ? values.build() : values.buildSorted(valueComparator);
          builder.put(key, list);
          size += list.size();
        }
    
        return new ImmutableListMultimap<>(builder.buildOrThrow(), size);
      }
}
