public class zxing_0249 {

      private static final int PATTERN_LENGTH = 5;
      private static final float PATTERN_MODULE_COUNT = 7.0f;
      private static final float MAX_VARIANCE_FACTOR = 2.0f;
      private static final float CENTER_MODULE_WEIGHT = 3.0f;
      private static final int CENTER_VARIANCE_MULTIPLIER = 3;

      protected static boolean foundPatternCross(int[] stateCount) {
        int totalModuleSize = 0;
        for (int i = 0; i < PATTERN_LENGTH; i++) {
          int count = stateCount[i];
          if (count == 0) {
            return false;
          }
          totalModuleSize += count;
        }
        if (totalModuleSize < PATTERN_MODULE_COUNT) {
          return false;
        }
        float moduleSize = totalModuleSize / PATTERN_MODULE_COUNT;
        float maxVariance = moduleSize / MAX_VARIANCE_FACTOR;
        return
            Math.abs(moduleSize - stateCount[0]) < maxVariance &&
            Math.abs(moduleSize - stateCount[1]) < maxVariance &&
            Math.abs(CENTER_MODULE_WEIGHT * moduleSize - stateCount[2]) < CENTER_VARIANCE_MULTIPLIER * maxVariance &&
            Math.abs(moduleSize - stateCount[3]) < maxVariance &&
            Math.abs(moduleSize - stateCount[4]) < maxVariance;
      }
}
