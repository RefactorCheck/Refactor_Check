public class ImmutableSortedMap {

      public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(
          K k1,
          V v1,
          K k2,
          V v2,
          K k3,
          V v3,
          K k4,
          V v4,
          K k5,
          V v5,
          K k6,
          V v6,
          K k7,
          V v7,
          K k8,
          V v8) {
        return fromEntries(
            entryOf(k1, v1),
            entryOf(k2, v2),
            entryOf(k3, v3),
            entryOf(k4, v4),
            entryOf(k5, v5),
            entryOf(k6, v6),
            entryOf(k7, v7),
            entryOf(k8, v8));
      }
}
