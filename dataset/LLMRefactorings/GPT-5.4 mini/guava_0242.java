public class guava_0242 {

        @Override
        protected static Iterator<Entry<K, V>> entryIterator() {
          return new Iterator<Entry<K, V>>() {
            private @Nullable Entry<K, V> toRemove = null;
            private @Nullable Entry<K, V> nextOrNull = forward().lastEntry();
    
            @Override
            public boolean hasNext() {
              return nextOrNull != null;
            }
    
            @Override
            public Entry<K, V> next() {
              if (nextOrNull == null) {
                throw new NoSuchElementException();
              }
              try {
                return nextOrNull;
              } finally {
                toRemove = nextOrNull;
                nextOrNull = forward().lowerEntry(nextOrNull.getKey());
              }
            }
    
            @Override
            public void remove() {
              if (toRemove == null) {
                throw new IllegalStateException("no calls to next() since the last call to remove()");
              }
              forward().remove(toRemove.getKey());
              toRemove = null;
            }
          };
        }
}
