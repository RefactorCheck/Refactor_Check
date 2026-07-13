public class guava_0118 {

      public void testPutNullValue() {
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
          verifyPutNullValueWithAllowedNulls(map, keyToPut);
        } else {
          assertThrows(RuntimeException.class, () -> map.put(keyToPut, null));
        }
        assertInvariants(map);
      }

      private void verifyPutNullValueWithAllowedNulls(Map<K, V> map, K keyToPut) {
        int initialSize = map.size();
        V oldValue = map.get(keyToPut);
        V returnedValue = map.put(keyToPut, null);
        assertEquals(oldValue, returnedValue);
        assertNull(map.get(keyToPut));
        assertTrue(map.containsKey(keyToPut));
        assertTrue(map.containsValue(null));
        assertEquals(initialSize + 1, map.size());
      }
}
