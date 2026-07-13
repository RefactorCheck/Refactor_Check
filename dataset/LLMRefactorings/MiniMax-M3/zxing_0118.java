public class zxing_0118 {

      private static int[] findAsteriskPattern(BitArray row, int[] counters) throws NotFoundException {
        int width = row.getSize();
        int rowOffset = row.getNextSet(0);
    
        int counterPosition = 0;
        int patternStart = rowOffset;
        boolean isWhite = false;
        int patternLength = counters.length;
    
        for (int i = rowOffset; i < width; i++) {
          if (row.get(i) != isWhite) {
            counters[counterPosition]++;
          } else {
            if (counterPosition == patternLength - 1) {
              if (matchesAsteriskPattern(row, counters, patternStart, i)) {
                return new int[]{patternStart, i};
              }
              patternStart += counters[0] + counters[1];
              System.arraycopy(counters, 2, counters, 0, counterPosition - 1);
              counters[counterPosition - 1] = 0;
              counters[counterPosition] = 0;
              counterPosition--;
            } else {
              counterPosition++;
            }
            counters[counterPosition] = 1;
            isWhite = !isWhite;
          }
        }
        throw NotFoundException.getNotFoundInstance();
      }
    
      private static boolean matchesAsteriskPattern(BitArray row, int[] counters, int patternStart, int currentIndex) throws NotFoundException {
        int startRange = Math.max(0, patternStart - ((currentIndex - patternStart) / 2));
        return toNarrowWidePattern(counters) == ASTERISK_ENCODING && row.isRange(startRange, patternStart, false);
      }
}
