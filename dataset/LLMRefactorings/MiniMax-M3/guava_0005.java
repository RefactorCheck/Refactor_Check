public class guava_0005 {

      @Override
      final UnmodifiableIterator<Entry<K, V>> entryIterator() {
        return new EntryIterator();
      }

      private final class EntryIterator extends UnmodifiableIterator<Entry<K, V>> {
        final Iterator<? extends Entry<K, ? extends ImmutableCollection<V>>> asMapItr =
            map.entrySet().iterator();
        @Nullable K currentKey = null;
        Iterator<V> valueItr = emptyIterator();

        void advance() {
          Entry<K, ? extends ImmutableCollection<V>> entry = asMapItr.next();
          currentKey = entry.getKey();
          valueItr = entry.getValue().iterator();
        }

        @Override
        public boolean hasNext() {
          return valueItr.hasNext() || asMapItr.hasNext();
        }

        @Override
        public Entry<K, V> next() {
          if (!valueItr.hasNext()) {
            advance();
          }
          /*
           * requireNonNull is safe: The first call to this method always enters the !hasNext() case
           * and populates currentKey, after which it's never cleared.
           */
          return immutableEntry(requireNonNull(currentKey), valueItr.next());
        }
      }
}
