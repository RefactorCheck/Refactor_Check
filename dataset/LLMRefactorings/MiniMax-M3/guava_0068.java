public class guava_0068 {

      @Override
      Set<K> createKeySet() {
        @WeakOuter
        final class KeySetImpl extends Sets.ImprovedAbstractSet<K> {
          @Override
          public int size() {
            return keyToKeyList.size();
          }
    
          @Override
          public Iterator<K> iterator() {
            return new DistinctKeyIterator();
          }
    
          @Override
          public boolean contains(@Nullable Object key) { // for performance
            return containsKey(key);
          }
    
          @Override
          public boolean remove(@Nullable Object o) { // for performance
            Collection<?> removed = LinkedListMultimap.this.removeAll(o);
            return !removed.isEmpty();
          }
        }
        return new KeySetImpl();
      }
}
