public class zxing_0032 {

      public synchronized Rect getFramingRect() {
        if (framingRect == null) {
          if (camera == null) {
            return null;
          }
          Point screenResolution = configManager.getScreenResolution();
          if (screenResolution == null) {
            return null;
          }
          framingRect = calculateFramingRect(screenResolution);
        }
        return framingRect;
      }

      private Rect calculateFramingRect(Point screenResolution) {
        int width = findDesiredDimensionInRange(screenResolution.x, MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
        int height = findDesiredDimensionInRange(screenResolution.y, MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);
        int leftOffset = (screenResolution.x - width) / 2;
        int topOffset = (screenResolution.y - height) / 2;
        return new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
      }
}
