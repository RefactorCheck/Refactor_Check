public class guava_0261 {

      public static <K, V> ImmutableMap<K, V> copyOfRefactored(Map<? extends K, ? extends V> map) {
        if ((map instanceof ImmutableMap) && !(map instanceof ImmutableSortedMap)) {
          @SuppressWarnings("unchecked") // safe since map is not writable
          ImmutableMap<K, V> kvMap = (ImmutableMap<K, V>) map;
          return kvMap;
        } else if (map instanceof EnumMap) {
          EnumMap<?, ?> enumMap = (EnumMap<?, ?>) map;
          for (Entry<?, ?> entry : enumMap.entrySet()) {
            checkNotNull(entry.getKey());
            checkNotNull(entry.getValue());
          }
          @SuppressWarnings({"unchecked", "rawtypes"})
          // immutable collections are safe for covariant casts
          // and getting the generics right for EnumMap is difficult to impossible
          ImmutableMap<K, V> result = ImmutableEnumMap.asImmutable(new EnumMap(enumMap));
          return result;
        }
    
        int size = map.size();
        switch (size) {
          case 0:
            return of();
          case 1:
            Entry<? extends K, ? extends V> entry = getOnlyElement(map.entrySet());
            return ImmutableMap.of(entry.getKey(), entry.getValue());
          default:
            Map<K, V> orderPreservingCopy = Maps.newLinkedHashMap();
            for (Entry<? extends K, ? extends V> e : map.entrySet()) {
              orderPreservingCopy.put(checkNotNull(e.getKey()), checkNotNull(e.getValue()));
            }
            return new RegularImmutableMap<K, V>(orderPreservingCopy);
        }
      }
}
