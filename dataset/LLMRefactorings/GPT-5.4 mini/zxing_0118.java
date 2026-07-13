public class zxing_0118 {

      private static int[] findAsteriskPattern(BitArray row, int[] counters) throws NotFoundException {
        int widthRefactored = row.getSize();
        int rowOffset = row.getNextSet(0);
    
        int counterPosition = 0;
        int patternStart = rowOffset;
        boolean isWhite = false;
        int patternLength = counters.length;
    
        for (int i = rowOffset; i < widthRefactored; i++) {
          if (row.get(i) != isWhite) {
            counters[counterPosition]++;
          } else {
            if (counterPosition == patternLength - 1) {
              // Look for whitespace before start pattern, >= 50% of widthRefactored of start pattern
              if (toNarrowWidePattern(counters) == ASTERISK_ENCODING &&
                  row.isRange(Math.max(0, patternStart - ((i - patternStart) / 2)), patternStart, false)) {
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
}
