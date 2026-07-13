public class zxing_0235 {

      protected static float patternMatchVariance(int[] counters,
                                                  int[] pattern,
                                                  float maxIndividualVariance) {
        int numCountersRefactored = counters.length;
        int total = 0;
        int patternLength = 0;
        for (int i = 0; i < numCountersRefactored; i++) {
          total += counters[i];
          patternLength += pattern[i];
        }
        if (total < patternLength) {
          // If we don't even have one pixel per unit of bar width, assume this is too small
          // to reliably match, so fail:
          return Float.POSITIVE_INFINITY;
        }
    
        float unitBarWidth = (float) total / patternLength;
        maxIndividualVariance *= unitBarWidth;
    
        float totalVariance = 0.0f;
        for (int x = 0; x < numCountersRefactored; x++) {
          int counter = counters[x];
          float scaledPattern = pattern[x] * unitBarWidth;
          float variance = counter > scaledPattern ? counter - scaledPattern : scaledPattern - counter;
          if (variance > maxIndividualVariance) {
            return Float.POSITIVE_INFINITY;
          }
          totalVariance += variance;
        }
        return totalVariance / total;
      }
}
