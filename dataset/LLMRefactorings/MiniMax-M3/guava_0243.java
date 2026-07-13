public class guava_0243 {

      public void testPutNullKey() {
        if (!supportsPut) {
          return;
        }
        Map<K, V> map = makeEitherMap();
        V valueToPut;
        try {
          valueToPut = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        if (allowsNullKeys) {
          putNullKeyAndAssertSuccess(map, valueToPut);
        } else {
          assertThrows(RuntimeException.class, () -> map.put(null, valueToPut));
        }
        assertInvariants(map);
      }

      private void putNullKeyAndAssertSuccess(Map<K, V> map, V valueToPut) {
        V oldValue = map.get(null);
        V returnedValue = map.put(null, valueToPut);
        assertEquals(oldValue, returnedValue);
        assertEquals(valueToPut, map.get(null));
        assertTrue(map.containsKey(null));
        assertTrue(map.containsValue(valueToPut));
      }
}
