public class guava_0118 {

      public static void testPutNullValue() {
        if (!supportsPut) {
          return;
        }
        Map<K, V> map = makeEitherMap();
        K keyToPut;
        try {
          keyToPut = getKeyNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        if (allowsNullValues) {
          int initialSize = map.size();
          V oldValue = map.get(keyToPut);
          V returnedValue = map.put(keyToPut, null);
          assertEquals(oldValue, returnedValue);
          assertNull(map.get(keyToPut));
          assertTrue(map.containsKey(keyToPut));
          assertTrue(map.containsValue(null));
          assertEquals(initialSize + 1, map.size());
        } else {
          assertThrows(RuntimeException.class, () -> map.put(keyToPut, null));
        }
        assertInvariants(map);
      }
}
