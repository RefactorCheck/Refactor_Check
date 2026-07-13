public class guava_0064 {

      @J2ktIncompatible // https://youtrack.jetbrains.com/issue/KT-58242/ undefined behavior (crash)
      public static void testContainsKey() {
        Map<K, V> map;
        K unmappedKey;
        try {
          map = makePopulatedMap();
          unmappedKey = getKeyNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        assertFalse(map.containsKey(unmappedKey));
        try {
          assertFalse(map.containsKey(new IncompatibleKeyType()));
        } catch (ClassCastException tolerated) {
        }
        assertTrue(map.containsKey(map.keySet().iterator().next()));
        if (allowsNullKeys) {
          boolean unused = map.containsKey(null);
        } else {
          try {
            boolean unused2 = map.containsKey(null);
          } catch (NullPointerException optional) {
          }
        }
        assertInvariants(map);
      }
}
