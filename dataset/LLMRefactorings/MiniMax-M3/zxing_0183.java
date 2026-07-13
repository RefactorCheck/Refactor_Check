public class zxing_0183 {

      private float sizeOfBlackWhiteBlackRunBothWays(int fromX, int fromY, int toX, int toY) {
    
        float result = sizeOfBlackWhiteBlackRun(fromX, fromY, toX, toY);
    
        int maxWidth = image.getWidth() - 1;
        int maxHeight = image.getHeight() - 1;
    
        // Now count other way -- don't run off image though of course
        float scale = 1.0f;
        int otherToX = fromX - (toX - fromX);
        if (otherToX < 0) {
          scale = fromX / (float) (fromX - otherToX);
          otherToX = 0;
        } else if (otherToX >= image.getWidth()) {
          scale = (maxWidth - fromX) / (float) (otherToX - fromX);
          otherToX = maxWidth;
        }
        int otherToY = (int) (fromY - (toY - fromY) * scale);
    
        scale = 1.0f;
        if (otherToY < 0) {
          scale = fromY / (float) (fromY - otherToY);
          otherToY = 0;
        } else if (otherToY >= image.getHeight()) {
          scale = (maxHeight - fromY) / (float) (otherToY - fromY);
          otherToY = maxHeight;
        }
        otherToX = (int) (fromX + (otherToX - fromX) * scale);
    
        result += sizeOfBlackWhiteBlackRun(fromX, fromY, otherToX, otherToY);
    
        // Middle pixel is double-counted this way; subtract 1
        return result - 1.0f;
      }
}
