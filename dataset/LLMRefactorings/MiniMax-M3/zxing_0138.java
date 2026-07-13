public class zxing_0138 {

      @Override
      public BitMatrix getBlackMatrix() throws NotFoundException {
        if (matrix != null) {
          return matrix;
        }
        LuminanceSource source = getLuminanceSource();
        int width = source.getWidth();
        int height = source.getHeight();
        if (width >= MINIMUM_DIMENSION && height >= MINIMUM_DIMENSION) {
          byte[] luminances = source.getMatrix();
          int subWidth = calculateSubDimension(width);
          int subHeight = calculateSubDimension(height);
          int[][] blackPoints = calculateBlackPoints(luminances, subWidth, subHeight, width, height);
    
          BitMatrix newMatrix = new BitMatrix(width, height);
          calculateThresholdForBlock(luminances, subWidth, subHeight, width, height, blackPoints, newMatrix);
          matrix = newMatrix;
        } else {
          matrix = super.getBlackMatrix();
        }
        return matrix;
      }
      
      private int calculateSubDimension(int dimension) {
        int subDimension = dimension >> BLOCK_SIZE_POWER;
        if ((dimension & BLOCK_SIZE_MASK) != 0) {
          subDimension++;
        }
        return subDimension;
      }
}
