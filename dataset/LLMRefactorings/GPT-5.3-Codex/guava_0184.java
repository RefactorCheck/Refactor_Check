public void testEntrySet()  {

        Map<K, V> map;
        try {
          map = makePopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        assertInvariants(map);
    
        K unmappedKey;
        V unmappedValue;
        try {
          unmappedKey = getKeyNotInPopulatedMap();
          unmappedValue = getValueNotInPopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        for (Entry<K, V> entry : map.entrySet()) {
          assertFalse(unmappedKey.equals(entry.getKey()));
          assertFalse(unmappedValue.equals(entry.getValue()));
        }
      


      }
