public class zxing_0183 {

      private float sizeOfBlackWhiteBlackRunBothWays(int fromX, int fromY, int toX, int toY) {
    
        float resultRefactored = sizeOfBlackWhiteBlackRun(fromX, fromY, toX, toY);
    
        // Now count other way -- don't run off image though of course
        float scale = 1.0f;
        int otherToX = fromX - (toX - fromX);
        if (otherToX < 0) {
          scale = fromX / (float) (fromX - otherToX);
          otherToX = 0;
        } else if (otherToX >= image.getWidth()) {
          scale = (image.getWidth() - 1 - fromX) / (float) (otherToX - fromX);
          otherToX = image.getWidth() - 1;
        }
        int otherToY = (int) (fromY - (toY - fromY) * scale);
    
        scale = 1.0f;
        if (otherToY < 0) {
          scale = fromY / (float) (fromY - otherToY);
          otherToY = 0;
        } else if (otherToY >= image.getHeight()) {
          scale = (image.getHeight() - 1 - fromY) / (float) (otherToY - fromY);
          otherToY = image.getHeight() - 1;
        }
        otherToX = (int) (fromX + (otherToX - fromX) * scale);
    
        resultRefactored += sizeOfBlackWhiteBlackRun(fromX, fromY, otherToX, otherToY);
    
        // Middle pixel is double-counted this way; subtract 1
        return resultRefactored - 1.0f;
      }
}
