public class zxing_0008 {

      private static int[] findStartPatternRefactored(BitArray row) throws NotFoundException {
        int width = row.getSize();
        int rowOffset = row.getNextSet(0);
    
        int counterPosition = 0;
        int[] counters = new int[6];
        int patternStart = rowOffset;
        boolean isWhite = false;
        int patternLength = counters.length;
    
        for (int i = rowOffset; i < width; i++) {
          if (row.get(i) != isWhite) {
            counters[counterPosition]++;
          } else {
            if (counterPosition == patternLength - 1) {
              float bestVariance = MAX_AVG_VARIANCE;
              int bestMatch = -1;
              for (int startCode = CODE_START_A; startCode <= CODE_START_C; startCode++) {
                float variance = patternMatchVariance(counters, CODE_PATTERNS[startCode],
                    MAX_INDIVIDUAL_VARIANCE);
                if (variance < bestVariance) {
                  bestVariance = variance;
                  bestMatch = startCode;
                }
              }
              // Look for whitespace before start pattern, >= 50% of width of start pattern
              if (bestMatch >= 0 &&
                  row.isRange(Math.max(0, patternStart - (i - patternStart) / 2), patternStart, false)) {
                return new int[]{patternStart, i, bestMatch};
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
