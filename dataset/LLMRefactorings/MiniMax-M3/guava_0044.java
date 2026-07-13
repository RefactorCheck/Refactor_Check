public class guava_0044 {

      public void testEntrySetRemoveDifferentValue() {
        Map<K, V> map;
        try {
          map = makePopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
    
        Set<Entry<K, V>> entrySet = map.entrySet();
        K key = map.keySet().iterator().next();
        Entry<K, V> entry = mapEntry(key, getValueNotInPopulatedMap());
        int initialSize = map.size();
        tryRemoveEntry(entrySet, entry);
        assertEquals(initialSize, map.size());
        assertTrue(map.containsKey(key));
        assertInvariants(map);
      }
    
      private void tryRemoveEntry(Set<Entry<K, V>> entrySet, Entry<K, V> entry) {
        try {
          boolean didRemove = entrySet.remove(entry);
          assertFalse(didRemove);
        } catch (UnsupportedOperationException optional) {
          if (supportsRemove) {
            throw optional;
          }
        }
      }
}
