public class guava_0244 {

      public void testPutAllNewKey() {
        Map<K, V> map = makeEitherMap();
        K keyToPut;
        V valueToPut;
        try {
          keyToPut = getKeyNotInPopulatedMap();
          valueToPut = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        Map<K, V> mapToPut = singletonMap(keyToPut, valueToPut);
        if (supportsPut) {
          verifyPutAll(map, mapToPut, keyToPut, valueToPut);
        } else {
          assertThrows(UnsupportedOperationException.class, () -> map.putAll(mapToPut));
        }
        assertInvariants(map);
      }

      private void verifyPutAll(Map<K, V> map, Map<K, V> mapToPut, K keyToPut, V valueToPut) {
        int initialSize = map.size();
        map.putAll(mapToPut);
        assertEquals(valueToPut, map.get(keyToPut));
        assertTrue(map.containsKey(keyToPut));
        assertTrue(map.containsValue(valueToPut));
        assertEquals(initialSize + 1, map.size());
      }
}
