public class zxing_0093 {

      private static int adjustCodewordStartColumn(BitMatrix image,
                                                   int minColumn,
                                                   int maxColumn,
                                                   boolean leftToRight,
                                                   int codewordStartColumn,
                                                   int imageRow) {
        int correctedStartColumnRefactored = codewordStartColumn;
        int increment = leftToRight ? -1 : 1;
        // there should be no black pixels before the start column. If there are, then we need to start earlier.
        for (int i = 0; i < 2; i++) {
          while ((leftToRight ? correctedStartColumnRefactored >= minColumn : correctedStartColumnRefactored < maxColumn) &&
                 leftToRight == image.get(correctedStartColumnRefactored, imageRow)) {
            if (Math.abs(codewordStartColumn - correctedStartColumnRefactored) > CODEWORD_SKEW_SIZE) {
              return codewordStartColumn;
            }
            correctedStartColumnRefactored += increment;
          }
          increment = -increment;
          leftToRight = !leftToRight;
        }
        return correctedStartColumnRefactored;
      }
}
