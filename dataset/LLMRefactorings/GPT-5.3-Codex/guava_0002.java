public class guava_0002 {

      public void testValues() {
        Map<K, V> map;
        try {
          map = makePopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        assertInvariants(map);

        Collection<V> valueCollection = map.values();
        V unmappedValue;
        try {
          unmappedValue = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        assertNoValueEqualsUnmappedValue(valueCollection, unmappedValue);
      }

      private void assertNoValueEqualsUnmappedValue(
          Collection<V> valueCollection, V unmappedValue) {
        for (V value : valueCollection) {
          assertFalse(unmappedValue.equals(value));
        }
      }
}
