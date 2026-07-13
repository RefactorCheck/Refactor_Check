private static final String EXTRACTED_STRING = "Expected UnsupportedOperationException or NullPointerException.";



      public void testKeySetRemoveAllNullFromEmpty()  {

        Map<K, V> map;
        try {
          map = makeEmptyMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
    
        Set<K> keySet = map.keySet();
        if (supportsRemove) {
          assertThrows(NullPointerException.class, () -> keySet.removeAll(null));
        } else {
          try {
            keySet.removeAll(null);
            fail(EXTRACTED_STRING);
          } catch (UnsupportedOperationException | NullPointerException e) {
            // Expected.
          }
        }
        assertInvariants(map);
      


      }
