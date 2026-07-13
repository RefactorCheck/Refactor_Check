public class guava_0087 {

      public void testEntrySetRetainAll() {
        Map<K, V> map;
        try {
          map = makePopulatedMap();
        } catch (UnsupportedOperationException e) {
          return;
        }
    
        Set<Entry<K, V>> entrySet = map.entrySet();
        Entry<K, V> originalEntry = entrySet.iterator().next();
        // Copy the Entry, as discussed in testEntrySetRemoveAll.
        Set<Entry<K, V>> entriesToRetain =
            singleton(mapEntry(originalEntry.getKey(), originalEntry.getValue()));
        if (supportsRemove) {
          boolean shouldRemove = entrySet.size() > entriesToRetain.size();
          boolean didRemove = entrySet.retainAll(entriesToRetain);
          assertEquals(shouldRemove, didRemove);
          assertEquals(entriesToRetain.size(), map.size());
          for (Entry<K, V> entry : entriesToRetain) {
            assertTrue(entrySet.contains(entry));
          }
        } else {
          assertThrows(UnsupportedOperationException.class, () -> entrySet.retainAll(entriesToRetain));
        }
        assertInvariants(map);
      }
}
