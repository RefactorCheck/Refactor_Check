public class guava_0025 {

      public void testTailMapWriteThrough() {
        SortedMap<K, V> map;
        try {
          map = makePopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        if (map.size() < 2 || !supportsPut) {
          return;
        }
        runTailMapWriteThroughTest(map);
      }
      
      private void runTailMapWriteThroughTest(SortedMap<K, V> map) {
        Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
        Entry<K, V> firstEntry = iterator.next();
        Entry<K, V> secondEntry = iterator.next();
        K key = secondEntry.getKey();
        SortedMap<K, V> subMap = map.tailMap(key);
        V value = getValueNotInPopulatedMap();
        subMap.put(key, value);
        assertEquals(secondEntry.getValue(), value);
        assertEquals(map.get(key), value);
        assertThrows(IllegalArgumentException.class, () -> subMap.put(firstEntry.getKey(), value));
      }
}
