public class zxing_0235 {

      protected static float patternMatchVariance(int[] counters,
                                                  int[] pattern,
                                                  float maxIndividualVariance) {
        int numCounters = counters.length;
        int total = 0;
        int patternLength = 0;
        for (int i = 0; i < numCounters; i++) {
          total += counters[i];
          patternLength += pattern[i];
        }
        if (total < patternLength) {
          return Float.POSITIVE_INFINITY;
        }

        float unitBarWidth = (float) total / patternLength;
        maxIndividualVariance *= unitBarWidth;

        float totalVariance = 0.0f;
        for (int x = 0; x < numCounters; x++) {
          float variance = counterVariance(counters[x], pattern[x] * unitBarWidth, maxIndividualVariance);
          if (variance == Float.POSITIVE_INFINITY) {
            return Float.POSITIVE_INFINITY;
          }
          totalVariance += variance;
        }
        return totalVariance / total;
      }

      private static float counterVariance(int counter, float scaledPattern, float maxAllowed) {
        float variance = counter > scaledPattern ? counter - scaledPattern : scaledPattern - counter;
        if (variance > maxAllowed) {
          return Float.POSITIVE_INFINITY;
        }
        return variance;
      }
}
