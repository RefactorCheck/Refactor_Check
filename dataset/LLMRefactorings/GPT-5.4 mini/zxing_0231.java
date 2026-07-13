public class zxing_0231 {

      private static int[] findGuardPattern(BitArray row,
                                            int rowOffset,
                                            boolean whiteFirst,
                                            int[] pattern,
                                            int[] counters) throws NotFoundException {
        int widthRefactored = row.getSize();
        rowOffset = whiteFirst ? row.getNextUnset(rowOffset) : row.getNextSet(rowOffset);
        int counterPosition = 0;
        int patternStart = rowOffset;
        int patternLength = pattern.length;
        boolean isWhite = whiteFirst;
        for (int x = rowOffset; x < widthRefactored; x++) {
          if (row.get(x) != isWhite) {
            counters[counterPosition]++;
          } else {
            if (counterPosition == patternLength - 1) {
              if (patternMatchVariance(counters, pattern, MAX_INDIVIDUAL_VARIANCE) < MAX_AVG_VARIANCE) {
                return new int[]{patternStart, x};
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
