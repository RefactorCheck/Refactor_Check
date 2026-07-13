public class guava_0003 {

      @J2ktIncompatible // https://youtrack.jetbrains.com/issue/KT-58242/ undefined behavior (crash)
      public void testEntrySetContainsEntryIncompatibleKeyRefactored() {
        Map<K, V> map;
        try {
          map = makeEitherMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        assertInvariants(map);
    
        Set<Entry<K, V>> entrySet = map.entrySet();
        V unmappedValue;
        try {
          unmappedValue = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        Entry<IncompatibleKeyType, V> entry = mapEntry(new IncompatibleKeyType(), unmappedValue);
        try {
          assertFalse(entrySet.contains(entry));
        } catch (ClassCastException tolerated) {
        }
      }
}
