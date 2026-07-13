public class zxing_0133 {

      static int applyMaskPenaltyRule3(ByteMatrix matrix) {
        int numPenalties = 0;
        byte[][] array = matrix.getArray();
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            byte[] arrayY = array[y];
            if (hasHorizontalFinderPattern(arrayY, x, width) &&
                (isWhiteHorizontal(arrayY, x - 4, x) || isWhiteHorizontal(arrayY, x + 7, x + 11))) {
              numPenalties++;
            }
            if (hasVerticalFinderPattern(array, x, y, height) &&
                (isWhiteVertical(array, x, y - 4, y) || isWhiteVertical(array, x, y + 7, y + 11))) {
              numPenalties++;
            }
          }
        }
        return numPenalties * N3;
      }

      private static boolean hasHorizontalFinderPattern(byte[] arrayY, int x, int width) {
        return x + 6 < width &&
            arrayY[x] == 1 &&
            arrayY[x + 1] == 0 &&
            arrayY[x + 2] == 1 &&
            arrayY[x + 3] == 1 &&
            arrayY[x + 4] == 1 &&
            arrayY[x + 5] == 0 &&
            arrayY[x + 6] == 1;
      }

      private static boolean hasVerticalFinderPattern(byte[][] array, int x, int y, int height) {
        return y + 6 < height &&
            array[y][x] == 1 &&
            array[y + 1][x] == 0 &&
            array[y + 2][x] == 1 &&
            array[y + 3][x] == 1 &&
            array[y + 4][x] == 1 &&
            array[y + 5][x] == 0 &&
            array[y + 6][x] == 1;
      }
}
