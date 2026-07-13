public class zxing_0155 {

      protected static void recordPattern(BitArray row,
                                          int start,
                                          int[] counters) throws NotFoundException {
        int numCounters = counters.length;
        Arrays.fill(counters, 0, numCounters, 0);
        int end = row.getSize();
        if (start >= end) {
          throw NotFoundException.getNotFoundInstance();
        }
        boolean isWhite = !row.get(start);
        int counterPosition = 0;
        int i = start;
        while (i < end) {
          if (row.get(i) != isWhite) {
            counters[counterPosition]++;
          } else {
            if (++counterPosition == numCounters) {
              break;
            } else {
              counters[counterPosition] = 1;
              isWhite = !isWhite;
            }
          }
          i++;
        }
        if (!isPatternValid(counterPosition, numCounters, i, end)) {
          throw NotFoundException.getNotFoundInstance();
        }
      }

      private static boolean isPatternValid(int counterPosition, int numCounters, int i, int end) {
        return counterPosition == numCounters || (counterPosition == numCounters - 1 && i == end);
      }
}
