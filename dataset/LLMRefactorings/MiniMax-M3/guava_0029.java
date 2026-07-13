public class guava_0029 {

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
          putNewEntryAndAssert(map, mapToPut, keyToPut, valueToPut);
        } else {
          assertThrows(UnsupportedOperationException.class, () -> map.putAll(mapToPut));
        }
        assertInvariants(map);
      }

      private void putNewEntryAndAssert(Map<K, V> map, Map<K, V> mapToPut, K keyToPut, V valueToPut) {
        int initialSize = map.size();
        map.putAll(mapToPut);
        assertEquals(valueToPut, map.get(keyToPut));
        assertTrue(map.containsKey(keyToPut));
        assertTrue(map.containsValue(valueToPut));
        assertEquals(initialSize + 1, map.size());
      }
}
