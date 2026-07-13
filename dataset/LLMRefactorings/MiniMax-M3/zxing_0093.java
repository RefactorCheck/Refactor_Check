public class zxing_0093 {

      private static int adjustCodewordStartColumn(BitMatrix image,
                                                   int minColumn,
                                                   int maxColumn,
                                                   boolean leftToRight,
                                                   int codewordStartColumn,
                                                   int imageRow) {
        int correctedStartColumn = codewordStartColumn;
        int increment = leftToRight ? -1 : 1;
        // there should be no black pixels before the start column. If there are, then we need to start earlier.
        for (int i = 0; i < 2; i++) {
          while (isWithinBounds(correctedStartColumn, minColumn, maxColumn, leftToRight) &&
                 leftToRight == image.get(correctedStartColumn, imageRow)) {
            if (Math.abs(codewordStartColumn - correctedStartColumn) > CODEWORD_SKEW_SIZE) {
              return codewordStartColumn;
            }
            correctedStartColumn += increment;
          }
          increment = -increment;
          leftToRight = !leftToRight;
        }
        return correctedStartColumn;
      }

      private static boolean isWithinBounds(int correctedStartColumn,
                                            int minColumn,
                                            int maxColumn,
                                            boolean leftToRight) {
        return leftToRight ? correctedStartColumn >= minColumn : correctedStartColumn < maxColumn;
      }
}
