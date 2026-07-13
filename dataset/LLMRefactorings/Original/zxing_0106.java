public class zxing_0106 {

      private static int decodeCode(BitArray row, int[] counters, int rowOffset)
          throws NotFoundException {
        recordPattern(row, rowOffset, counters);
        float bestVariance = MAX_AVG_VARIANCE; // worst variance we'll accept
        int bestMatch = -1;
        for (int d = 0; d < CODE_PATTERNS.length; d++) {
          int[] pattern = CODE_PATTERNS[d];
          float variance = patternMatchVariance(counters, pattern, MAX_INDIVIDUAL_VARIANCE);
          if (variance < bestVariance) {
            bestVariance = variance;
            bestMatch = d;
          }
        }
        // TODO We're overlooking the fact that the STOP pattern has 7 values, not 6.
        if (bestMatch >= 0) {
          return bestMatch;
        } else {
          throw NotFoundException.getNotFoundInstance();
        }
      }
}
