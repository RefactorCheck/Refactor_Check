public class zxing_0107 {

      private static DetectionResultRowIndicatorColumn getRowIndicatorColumn(BitMatrix image,
                                                                             BoundingBox boundingBox,
                                                                             ResultPoint startPoint,
                                                                             boolean leftToRight,
                                                                             int minCodewordWidth,
                                                                             int maxCodewordWidth) {
        DetectionResultRowIndicatorColumn rowIndicatorColumn = new DetectionResultRowIndicatorColumn(boundingBox,
            leftToRight);
        for (int i = 0; i < 2; i++) {
          int increment = i == 0 ? 1 : -1;
          int startColumn = (int) startPoint.getX();
          for (int imageRow = (int) startPoint.getY(); imageRow <= boundingBox.getMaxY() &&
              imageRow >= boundingBox.getMinY(); imageRow += increment) {
            Codeword codeword = detectCodeword(image, 0, image.getWidth(), leftToRight, startColumn, imageRow,
                minCodewordWidth, maxCodewordWidth);
            if (codeword != null) {
              rowIndicatorColumn.setCodeword(imageRow, codeword);
              if (leftToRight) {
                startColumn = codeword.getStartX();
              } else {
                startColumn = codeword.getEndX();
              }
            }
          }
        }
        return rowIndicatorColumn;
      }
}
