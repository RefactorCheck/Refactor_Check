public class guava_0028 {

        @Override
        public final Iterator<K> iterator() {
          Iterator<Entry<K, Collection<V>>> entryIterator = map().entrySet().iterator();
          return new Iterator<K>() {
            @Nullable Entry<K, Collection<V>> entry;
    
            @Override
            public boolean hasNext() {
              return entryIterator.hasNext();
            }
    
            @Override
            @ParametricNullness
            public K next() {
              entry = entryIterator.next();
              return entry.getKey();
            }
    
            @Override
            public void remove() {
              checkState(entry != null, "no calls to next() since the last call to remove()");
              Collection<V> collection = entry.getValue();
              entryIterator.remove();
              totalSize -= collection.size();
              collection.clear();
              entry = null;
            }
          };
        }
}
