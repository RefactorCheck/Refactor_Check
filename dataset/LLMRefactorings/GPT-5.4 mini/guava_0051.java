public class guava_0051 {

      public void testPutAllExistingKeyRefactored() {
        Map<K, V> map;
        V valueToPut;
        try {
          map = makePopulatedMap();
          valueToPut = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        K keyToPut = map.keySet().iterator().next();
        Map<K, V> mapToPut = singletonMap(keyToPut, valueToPut);
        int initialSize = map.size();
        if (supportsPut) {
          map.putAll(mapToPut);
          assertEquals(valueToPut, map.get(keyToPut));
          assertTrue(map.containsKey(keyToPut));
          assertTrue(map.containsValue(valueToPut));
        } else {
          assertThrows(UnsupportedOperationException.class, () -> map.putAll(mapToPut));
        }
        assertEquals(initialSize, map.size());
        assertInvariants(map);
      }
}
