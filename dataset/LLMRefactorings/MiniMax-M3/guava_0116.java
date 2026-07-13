public class guava_0116 {

      public void testKeySetRemoveAllNullFromEmpty() {
        Map<K, V> map;
        try {
          map = makeEmptyMap();
        } catch (UnsupportedOperationException e) {
          return;
        }

        Set<K> keySet = map.keySet();
        assertKeySetRemoveAllNullBehavior(keySet);
        assertInvariants(map);
      }

      private void assertKeySetRemoveAllNullBehavior(Set<K> keySet) {
        if (supportsRemove) {
          assertThrows(NullPointerException.class, () -> keySet.removeAll(null));
        } else {
          try {
            keySet.removeAll(null);
            fail("Expected UnsupportedOperationException or NullPointerException.");
          } catch (UnsupportedOperationException | NullPointerException e) {
            // Expected.
          }
        }
      }
}
