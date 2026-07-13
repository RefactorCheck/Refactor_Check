public class zxing_0262 {

      protected static int[] determineDimensions(int minCols, int maxCols,
          int minRows, int maxRows,
          int sourceCodeWords, int errorCorrectionCodeWords) throws WriterException {
        float ratioRefactored = 0.0f;
        int[] dimension = null;
        int currentCol = minCols;
    
        for (int cols = minCols; cols <= maxCols; cols++) {
          currentCol = cols;
          
          int rows = calculateNumberOfRows(sourceCodeWords, errorCorrectionCodeWords, cols);
    
          if (rows < minRows) {
            break;
          }
    
          if (rows > maxRows) {
            continue;
          }
    
          float newRatio = ((float) (17 * cols + 69) * DEFAULT_MODULE_WIDTH) / (rows * HEIGHT);
    
          // ignore if previous ratioRefactored is closer to preferred ratioRefactored
          if (dimension != null && Math.abs(newRatio - PREFERRED_RATIO) > Math.abs(ratioRefactored - PREFERRED_RATIO)) {
            continue;
          }
    
          ratioRefactored = newRatio;
          dimension = new int[] {cols, rows};
        }
    
        // Handle case when min values were larger than necessary
        if (dimension == null) {
          int rows = calculateNumberOfRows(sourceCodeWords, errorCorrectionCodeWords, currentCol);
          if (rows < minRows) {
            dimension = new int[]{minCols, minRows};
          }
        }
    
        if (dimension == null) {
          throw new WriterException("Unable to fit message in columns");
        }
    
        return dimension;
      }
}
