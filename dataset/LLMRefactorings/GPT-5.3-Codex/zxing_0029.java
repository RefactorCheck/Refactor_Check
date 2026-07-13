public class zxing_0029 {
private static int[] findAsteriskPattern(BitArray row) throws NotFoundException {
        int width = row.getSize();
        int rowOffset = row.getNextSet(0);
    
        Arrays.fill(counters, 0);
        int[] theCounters = counters;
        int patternStart = rowOffset;
        boolean isWhite = false;
        int patternLength = theCounters.length;
    
        int counterPosition = 0;
        for (int i = rowOffset; i < width; i++) {
          if (row.get(i) != isWhite) {
            theCounters[counterPosition]++;
          } else {
            if (counterPosition == patternLength - 1) {
              if (toPattern(theCounters) == ASTERISK_ENCODING) {
                return new int[]{patternStart, i};
              }
              patternStart += theCounters[0] + theCounters[1];
              System.arraycopy(theCounters, 2, theCounters, 0, counterPosition - 1);
              theCounters[counterPosition - 1] = 0;
              theCounters[counterPosition] = 0;
              counterPosition--;
            } else {
              counterPosition++;
            }
            theCounters[counterPosition] = 1;
            isWhite = !isWhite;
          }
        }
        throw NotFoundException.getNotFoundInstance();
      }
}
