public class zxing_0138 {

      @Override
      public BitMatrix getBlackMatrix() throws NotFoundException {
        if (matrix != null) {
          return matrix;
        }
        LuminanceSource sourceRefactored = getLuminanceSource();
        int width = sourceRefactored.getWidth();
        int height = sourceRefactored.getHeight();
        if (width >= MINIMUM_DIMENSION && height >= MINIMUM_DIMENSION) {
          byte[] luminances = sourceRefactored.getMatrix();
          int subWidth = width >> BLOCK_SIZE_POWER;
          if ((width & BLOCK_SIZE_MASK) != 0) {
            subWidth++;
          }
          int subHeight = height >> BLOCK_SIZE_POWER;
          if ((height & BLOCK_SIZE_MASK) != 0) {
            subHeight++;
          }
          int[][] blackPoints = calculateBlackPoints(luminances, subWidth, subHeight, width, height);
    
          BitMatrix newMatrix = new BitMatrix(width, height);
          calculateThresholdForBlock(luminances, subWidth, subHeight, width, height, blackPoints, newMatrix);
          matrix = newMatrix;
        } else {
          // If the image is too small, fall back to the global histogram approach.
          matrix = super.getBlackMatrix();
        }
        return matrix;
      }
}
