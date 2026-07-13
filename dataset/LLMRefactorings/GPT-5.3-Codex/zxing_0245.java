public class zxing_0245 {

      private static ResultPoint[] findVertices(BitMatrix matrix, int startRow, int startColumn, boolean normalize) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
    
        ResultPoint[] result = new ResultPoint[8];
        int minHeight = BARCODE_MIN_HEIGHT;
        copyToResult(result, findRowsWithPattern(matrix, height, width, startRow, startColumn, minHeight, START_PATTERN),
            INDEXES_START_PATTERN);
    
        if (result[4] != null) {
          startColumn = (int) result[4].getX();
          startRow = (int) result[4].getY();
          if (result[5] != null) {
            int endRow = (int) result[5].getY();
            int startPatternHeight = endRow - startRow;
            minHeight = (int) Math.max(startPatternHeight * MAX_STOP_PATTERN_HEIGHT_VARIANCE, BARCODE_MIN_HEIGHT);
          }
        }
        copyToResult(result, findRowsWithPattern(matrix, height, width, startRow, startColumn, minHeight, STOP_PATTERN),
            INDEXES_STOP_PATTERN);
        return result;
      }
}
