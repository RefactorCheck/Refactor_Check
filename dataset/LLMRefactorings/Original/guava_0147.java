public class guava_0147 {

      public static <K, V> ImmutableMap<K, V> of(
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
          V v8,
          K k9,
          V v9,
          K k10,
          V v10) {
        return new RegularImmutableMap<K, V>(
            entryOf(k1, v1),
            entryOf(k2, v2),
            entryOf(k3, v3),
            entryOf(k4, v4),
            entryOf(k5, v5),
            entryOf(k6, v6),
            entryOf(k7, v7),
            entryOf(k8, v8),
            entryOf(k9, v9),
            entryOf(k10, v10));
      }
}
