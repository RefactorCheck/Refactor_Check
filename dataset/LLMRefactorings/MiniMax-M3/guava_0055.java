public class guava_0055 {

      public void testPutNewKey() {
        Map<K, V> map = makeEitherMap();
        K keyToPut;
        V valueToPut;
        try {
          keyToPut = getKeyNotInPopulatedMap();
          valueToPut = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        executePut(map, keyToPut, valueToPut);
        assertInvariants(map);
      }

      private void executePut(Map<K, V> map, K keyToPut, V valueToPut) {
        if (supportsPut) {
          int initialSize = map.size();
          V oldValue = map.put(keyToPut, valueToPut);
          assertEquals(valueToPut, map.get(keyToPut));
          assertTrue(map.containsKey(keyToPut));
          assertTrue(map.containsValue(valueToPut));
          assertEquals(initialSize + 1, map.size());
          assertNull(oldValue);
        } else {
          assertThrows(UnsupportedOperationException.class, () -> map.put(keyToPut, valueToPut));
        }
      }
}
