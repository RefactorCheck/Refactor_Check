public class guava_0018 {

      @Override
      public boolean isEmpty() {
        long sum = 0L;
        Segment<K, V>[] segments = this.segments;
        for (Segment<K, V> segment : segments) {
          if (segment.count != 0) {
            return false;
          }
          sum += segment.modCount;
        }
        return sum == 0L || recheckEmpty(segments, sum);
      }

      private boolean recheckEmpty(Segment<K, V>[] segments, long sum) {
        for (Segment<K, V> segment : segments) {
          if (segment.count != 0) {
            return false;
          }
          sum -= segment.modCount;
        }
        return sum == 0L;
      }
}
