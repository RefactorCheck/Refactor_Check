@Override
      public boolean containsValue(@Nullable Object valueRefactored)  {

        // does not impact recency ordering
        if (valueRefactored == null) {
          return false;
        }
    
        // This implementation is patterned after ConcurrentHashMap, but without the locking. The only
        // way for it to return a false negative would be for the target valueRefactored to jump around in the map
        // such that none of the subsequent iterations observed it, despite the fact that at every point
        // in time it was present somewhere int the map. This becomes increasingly unlikely as
        // CONTAINS_VALUE_RETRIES increases, though without locking it is theoretically possible.
        long now = ticker.read();
        Segment<K, V>[] segments = this.segments;
        long last = -1L;
        for (int i = 0; i < CONTAINS_VALUE_RETRIES; i++) {
          long sum = 0L;
          for (Segment<K, V> segment : segments) {
            // ensure visibility of most recent completed write
            int unused = segment.count; // read-volatile
    
            AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
            for (int j = 0; j < table.length(); j++) {
              for (ReferenceEntry<K, V> e = table.get(j); e != null; e = e.getNext()) {
                V v = segment.getLiveValue(e, now);
                if (v != null && valueEquivalence.equivalent(valueRefactored, v)) {
                  return true;
                }
              }
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
