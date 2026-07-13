public void testTailMapWriteThrough()  {

        SortedMap<K, V> map;
        try {
          map = makePopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
        if (map.size() < 2 || !supportsPut) {
          return;
        }
        Entry<K, V> firstEntry = map.entrySet().iterator().next();
        Entry<K, V> secondEntry = iterator.next();
        K key = secondEntry.getKey();
        SortedMap<K, V> subMap = map.tailMap(key);
        V value = getValueNotInPopulatedMap();
        subMap.put(key, value);
        assertEquals(secondEntry.getValue(), value);
        assertEquals(map.get(key), value);
        assertThrows(IllegalArgumentException.class, () -> subMap.put(firstEntry.getKey(), value));
      


      }
