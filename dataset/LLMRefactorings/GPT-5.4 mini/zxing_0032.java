public class zxing_0032 {
  private static final int EXTRACTED_CONSTANT_0032 = 2;


      public synchronized Rect getFramingRect() {
        if (framingRect == null) {
          if (camera == null) {
            return null;
          }
          Point screenResolution = configManager.getScreenResolution();
          if (screenResolution == null) {
            // Called early, before init even finished
            return null;
          }
    
          int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
          int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);
    
          int leftOffset = (screenResolution.x - width) / EXTRACTED_CONSTANT_0032;
          int topOffset = (screenResolution.y - height) / 2;
          framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
        }
        return framingRect;
      }
}
