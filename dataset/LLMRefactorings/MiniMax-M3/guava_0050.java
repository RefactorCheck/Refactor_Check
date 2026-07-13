public class guava_0050 {

      static <K, V> ImmutableBiMap<K, V> create(int n, @Nullable Entry<K, V>[] entryArray) {
        Map<K, V> forwardDelegate = newHashMapWithExpectedSize(n);
        Map<V, K> backwardDelegate = newHashMapWithExpectedSize(n);
        for (int i = 0; i < n; i++) {
          putEntry(forwardDelegate, backwardDelegate, entryArray, i);
        }
        ImmutableList<Entry<K, V>> entryList = asImmutableList(entryArray, n);
        return new JdkBackedImmutableBiMap<>(
            entryList, forwardDelegate, backwardDelegate, /* inverse= */ null);
      }

      private static <K, V> void putEntry(
          Map<K, V> forwardDelegate,
          Map<V, K> backwardDelegate,
          @Nullable Entry<K, V>[] entryArray,
          int i) {
        Entry<K, V> e = RegularImmutableMap.makeImmutable(requireNonNull(entryArray[i]));
        entryArray[i] = e;
        V oldValue = forwardDelegate.putIfAbsent(e.getKey(), e.getValue());
        if (oldValue != null) {
          throw conflictException("key", e.getKey() + "=" + oldValue, entryArray[i]);
        }
        K oldKey = backwardDelegate.putIfAbsent(e.getValue(), e.getKey());
        if (oldKey != null) {
          throw conflictException("value", oldKey + "=" + e.getValue(), entryArray[i]);
        }
      }
}
