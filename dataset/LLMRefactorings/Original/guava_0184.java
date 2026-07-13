public class guava_0184 {

      public void testEntrySet() {
        Map<K, V> map;
        try {
          map = makePopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        assertInvariants(map);
    
        Set<Entry<K, V>> entrySet = map.entrySet();
        K unmappedKey;
        V unmappedValue;
        try {
          unmappedKey = getKeyNotInPopulatedMap();
          unmappedValue = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        for (Entry<K, V> entry : entrySet) {
          assertFalse(unmappedKey.equals(entry.getKey()));
          assertFalse(unmappedValue.equals(entry.getValue()));
        }
      }
}
