public class guava_0098 {

      public static void testEntrySetRemoveNullKeyPresent() {
        if (!allowsNullKeys || !supportsPut || !supportsRemove) {
          return;
        }
        Map<K, V> map;
        try {
          map = makeEitherMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        assertInvariants(map);
    
        Set<Entry<K, V>> entrySet = map.entrySet();
        V unmappedValue;
        try {
          unmappedValue = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
    
        map.put(null, unmappedValue);
        assertEquals(unmappedValue, map.get(null));
        assertTrue(map.containsKey(null));
        Entry<@Nullable K, V> entry = mapEntry(null, unmappedValue);
        assertTrue(entrySet.remove(entry));
        assertNull(map.get(null));
        assertFalse(map.containsKey(null));
      }
}
