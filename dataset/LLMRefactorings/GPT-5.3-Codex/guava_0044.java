private Set<Entry<K, V>> entrySet = map.entrySet();



      public void testEntrySetRemoveDifferentValue()  {

        Map<K, V> map;
        try {
          map = makePopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
    
        K key = map.keySet().iterator().next();
        Entry<K, V> entry = mapEntry(key, getValueNotInPopulatedMap());
        int initialSize = map.size();
        if (supportsRemove) {
          boolean didRemove = entrySet.remove(entry);
          assertFalse(didRemove);
        } else {
          try {
            boolean didRemove = entrySet.remove(entry);
            assertFalse(didRemove);
          } catch (UnsupportedOperationException optional) {
          }
        }
        assertEquals(initialSize, map.size());
        assertTrue(map.containsKey(key));
        assertInvariants(map);
      


      }
