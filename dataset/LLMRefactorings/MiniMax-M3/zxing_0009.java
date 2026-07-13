public class zxing_0009 {

      private static int applyMaskPenaltyRule1Internal(ByteMatrix matrix, boolean isHorizontal) {
        int penalty = 0;
        int iLimit = isHorizontal ? matrix.getHeight() : matrix.getWidth();
        int jLimit = isHorizontal ? matrix.getWidth() : matrix.getHeight();
        byte[][] array = matrix.getArray();
        for (int i = 0; i < iLimit; i++) {
          int numSameBitCells = 0;
          int prevBit = -1;
          for (int j = 0; j < jLimit; j++) {
            int bit = isHorizontal ? array[i][j] : array[j][i];
            if (bit == prevBit) {
              numSameBitCells++;
            } else {
              penalty = addPenaltyForRun(penalty, numSameBitCells);
              numSameBitCells = 1;
              prevBit = bit;
            }
          }
          penalty = addPenaltyForRun(penalty, numSameBitCells);
        }
        return penalty;
      }

      private static int addPenaltyForRun(int penalty, int runLength) {
        if (runLength >= 5) {
          return penalty + N1 + (runLength - 5);
        }
        return penalty;
      }
}
