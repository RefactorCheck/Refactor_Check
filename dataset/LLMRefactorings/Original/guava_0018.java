public class guava_0018 {

      @Override
      public boolean isEmpty() {
        /*
         * Sum per-segment modCounts to avoid mis-reporting when elements are concurrently added and
         * removed in one segment while checking another, in which case the table was never actually
         * empty at any point. (The sum ensures accuracy up through at least 1<<31 per-segment
         * modifications before recheck.) Method containsValue() uses similar constructions for
         * stability checks.
         */
        long sum = 0L;
        Segment<K, V>[] segments = this.segments;
        for (Segment<K, V> segment : segments) {
          if (segment.count != 0) {
            return false;
          }
          sum += segment.modCount;
        }
    
        if (sum != 0L) { // recheck unless no modifications
          for (Segment<K, V> segment : segments) {
            if (segment.count != 0) {
              return false;
            }
            sum -= segment.modCount;
          }
          return sum == 0L;
        }
        return true;
      }
}
