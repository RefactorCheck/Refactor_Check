public class guava_0068 {

      @Override
      static Set<K> createKeySet() {
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
            return !LinkedListMultimap.this.removeAll(o).isEmpty();
          }
        }
        return new KeySetImpl();
      }
}
