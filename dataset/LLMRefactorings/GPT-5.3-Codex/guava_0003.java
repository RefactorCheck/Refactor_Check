public class guava_0003 {

      @J2ktIncompatible
      public void testEntrySetContainsEntryIncompatibleKey() {
        Map<K, V> map;
        try {
          map = makeEitherMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        assertInvariants(map);

        Set<Entry<K, V>> entrySet = map.entrySet();
        V missingValue;
        try {
          missingValue = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        Entry<IncompatibleKeyType, V> incompatibleEntry = mapEntry(new IncompatibleKeyType(), missingValue);
        try {
          assertFalse(entrySet.contains(incompatibleEntry));
        } catch (ClassCastException tolerated) {
        }
      }
}
