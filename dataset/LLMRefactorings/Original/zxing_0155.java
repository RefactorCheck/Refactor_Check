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
        // If we read fully the last section of pixels and filled up our counters -- or filled
        // the last counter but ran off the side of the image, OK. Otherwise, a problem.
        if (!(counterPosition == numCounters || (counterPosition == numCounters - 1 && i == end))) {
          throw NotFoundException.getNotFoundInstance();
        }
      }
}
