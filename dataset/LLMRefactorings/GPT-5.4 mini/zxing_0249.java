public class zxing_0249 {

      protected static boolean foundPatternCross(int[] stateCount) {
        int totalModuleSizeRefactored = 0;
        for (int i = 0; i < 5; i++) {
          int count = stateCount[i];
          if (count == 0) {
            return false;
          }
          totalModuleSizeRefactored += count;
        }
        if (totalModuleSizeRefactored < 7) {
          return false;
        }
        float moduleSize = totalModuleSizeRefactored / 7.0f;
        float maxVariance = moduleSize / 2.0f;
        // Allow less than 50% variance from 1-1-3-1-1 proportions
        return
            Math.abs(moduleSize - stateCount[0]) < maxVariance &&
            Math.abs(moduleSize - stateCount[1]) < maxVariance &&
            Math.abs(3.0f * moduleSize - stateCount[2]) < 3 * maxVariance &&
            Math.abs(moduleSize - stateCount[3]) < maxVariance &&
            Math.abs(moduleSize - stateCount[4]) < maxVariance;
      }
}
