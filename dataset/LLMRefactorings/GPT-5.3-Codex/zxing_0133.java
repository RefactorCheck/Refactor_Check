public class zxing_0133 {
private static final int DEFAULT_INT = 0;


      static int applyMaskPenaltyRule3(ByteMatrix matrix) {
        int numPenalties = DEFAULT_INT;
        byte[][] array = matrix.getArray();
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            byte[] arrayY = array[y];  // We can at least optimize this access
            if (x + 6 < width &&
                arrayY[x] == 1 &&
                arrayY[x + 1] == 0 &&
                arrayY[x + 2] == 1 &&
                arrayY[x + 3] == 1 &&
                arrayY[x + 4] == 1 &&
                arrayY[x + 5] == 0 &&
                arrayY[x + 6] == 1 &&
                (isWhiteHorizontal(arrayY, x - 4, x) || isWhiteHorizontal(arrayY, x + 7, x + 11))) {
              numPenalties++;
            }
            if (y + 6 < height &&
                array[y][x] == 1 &&
                array[y + 1][x] == 0 &&
                array[y + 2][x] == 1 &&
                array[y + 3][x] == 1 &&
                array[y + 4][x] == 1 &&
                array[y + 5][x] == 0 &&
                array[y + 6][x] == 1 &&
                (isWhiteVertical(array, x, y - 4, y) || isWhiteVertical(array, x, y + 7, y + 11))) {
              numPenalties++;
            }
          }
        }
        return numPenalties * N3;
      }
}
