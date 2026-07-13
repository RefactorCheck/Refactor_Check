public class guava_0005 {

      @Override
      final UnmodifiableIterator<Entry<K, V>> entryIterator() {
        return new UnmodifiableIterator<Entry<K, V>>() {
          final Iterator<? extends Entry<K, ? extends ImmutableCollection<V>>> mapEntries =
              map.entrySet().iterator();
          @Nullable K currentKey = null;
          Iterator<V> values = emptyIterator();

          @Override
          public boolean hasNext() {
            return values.hasNext() || mapEntries.hasNext();
          }

          @Override
          public Entry<K, V> next() {
            if (!values.hasNext()) {
              Entry<K, ? extends ImmutableCollection<V>> entry = mapEntries.next();
              currentKey = entry.getKey();
              values = entry.getValue().iterator();
            }
            return immutableEntry(requireNonNull(currentKey), values.next());
          }
        };
      }
}
