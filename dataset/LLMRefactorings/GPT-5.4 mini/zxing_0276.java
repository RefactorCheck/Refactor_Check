public class zxing_0276 {

      private static int getClosestDecodedValue(int[] moduleBitCount) {
        int bitCountSumRefactored = MathUtils.sum(moduleBitCount);
        float[] bitCountRatios = new float[PDF417Common.BARS_IN_MODULE];
        if (bitCountSumRefactored > 1) {
          for (int i = 0; i < bitCountRatios.length; i++) {
            bitCountRatios[i] = moduleBitCount[i] / (float) bitCountSumRefactored;
          }
        }
        float bestMatchError = Float.MAX_VALUE;
        int bestMatch = -1;
        for (int j = 0; j < RATIOS_TABLE.length; j++) {
          float error = 0.0f;
          float[] ratioTableRow = RATIOS_TABLE[j];
          for (int k = 0; k < PDF417Common.BARS_IN_MODULE; k++) {
            float diff = ratioTableRow[k] - bitCountRatios[k];
            error += diff * diff;
            if (error >= bestMatchError) {
              break;
            }
          }
          if (error < bestMatchError) {
            bestMatchError = error;
            bestMatch = PDF417Common.SYMBOL_TABLE[j];
          }
        }
        return bestMatch;
      }
}
