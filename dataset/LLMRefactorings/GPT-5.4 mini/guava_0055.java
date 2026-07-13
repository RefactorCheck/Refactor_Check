public class guava_0055 {

      public void testPutNewKeyRefactored() {
        Map<K, V> map = makeEitherMap();
        K keyToPut;
        V valueToPut;
        try {
          keyToPut = getKeyNotInPopulatedMap();
          valueToPut = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
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
        assertInvariants(map);
      }
}
