public class zxing_0213 {

      public synchronized Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
          Rect framingRectRefactored = getFramingRect();
          if (framingRectRefactored == null) {
            return null;
          }
          Rect rect = new Rect(framingRectRefactored);
          Point cameraResolution = configManager.getCameraResolution();
          Point screenResolution = configManager.getScreenResolution();
          if (cameraResolution == null || screenResolution == null) {
            // Called early, before init even finished
            return null;
          }
          rect.left = rect.left * cameraResolution.x / screenResolution.x;
          rect.right = rect.right * cameraResolution.x / screenResolution.x;
          rect.top = rect.top * cameraResolution.y / screenResolution.y;
          rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
          framingRectInPreview = rect;
        }
        return framingRectInPreview;
      }
}
