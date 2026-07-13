public class zxing_0014 {

      private static final int PATTERN_LENGTH = 5;
      private static final float MIN_MODULE_SIZE = 7.0f;
      private static final float VARIANCE_DIVISOR = 1.333f;
      private static final int CENTER_MULTIPLIER = 3;

      protected static boolean foundPatternDiagonal(int[] stateCount) {
        int totalModuleSize = 0;
        for (int i = 0; i < PATTERN_LENGTH; i++) {
          int count = stateCount[i];
          if (count == 0) {
            return false;
          }
          totalModuleSize += count;
        }
        if (totalModuleSize < MIN_MODULE_SIZE) {
          return false;
        }
        float moduleSize = totalModuleSize / MIN_MODULE_SIZE;
        float maxVariance = moduleSize / VARIANCE_DIVISOR;
        return
                Math.abs(moduleSize - stateCount[0]) < maxVariance &&
                        Math.abs(moduleSize - stateCount[1]) < maxVariance &&
                        Math.abs(CENTER_MULTIPLIER * moduleSize - stateCount[2]) < CENTER_MULTIPLIER * maxVariance &&
                        Math.abs(moduleSize - stateCount[3]) < maxVariance &&
                        Math.abs(moduleSize - stateCount[4]) < maxVariance;
      }
}
