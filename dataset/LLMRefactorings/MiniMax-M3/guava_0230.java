public class guava_0230 {

      @Override
      public boolean containsValue(@Nullable Object value) {
        // does not impact recency ordering
        if (value == null) {
          return false;
        }
    
        // This implementation is patterned after ConcurrentHashMap, but without the locking. The only
        // way for it to return a false negative would be for the target value to jump around in the map
        // such that none of the subsequent iterations observed it, despite the fact that at every point
        // in time it was present somewhere int the map. This becomes increasingly unlikely as
        // CONTAINS_VALUE_RETRIES increases, though without locking it is theoretically possible.
        long now = ticker.read();
        Segment<K, V>[] segments = this.segments;
        long last = -1L;
        for (int i = 0; i < CONTAINS_VALUE_RETRIES; i++) {
          long sum = 0L;
          for (Segment<K, V> segment : segments) {
            if (containsValueInSegment(segment, value, now)) {
              return true;
            }
            sum += segment.modCount;
          }
          if (sum == last) {
            break;
          }
          last = sum;
        }
        return false;
      }

      private boolean containsValueInSegment(Segment<K, V> segment, Object value, long now) {
        // ensure visibility of most recent completed write
        int unused = segment.count; // read-volatile
    
        AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
        for (int j = 0; j < table.length(); j++) {
          for (ReferenceEntry<K, V> e = table.get(j); e != null; e = e.getNext()) {
            V v = segment.getLiveValue(e, now);
            if (v != null && valueEquivalence.equivalent(value, v)) {
              return true;
            }
          }
        }
        return false;
      }
}
