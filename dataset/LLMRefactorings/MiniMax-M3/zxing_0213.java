public class zxing_0213 {

      public synchronized Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
          Rect framingRect = getFramingRect();
          if (framingRect == null) {
            return null;
          }
          Rect rect = new Rect(framingRect);
          Point cameraResolution = configManager.getCameraResolution();
          Point screenResolution = configManager.getScreenResolution();
          if (cameraResolution == null || screenResolution == null) {
            return null;
          }
          scaleRectToPreview(rect, cameraResolution, screenResolution);
          framingRectInPreview = rect;
        }
        return framingRectInPreview;
      }

      private static void scaleRectToPreview(Rect rect, Point cameraResolution, Point screenResolution) {
        rect.left = rect.left * cameraResolution.x / screenResolution.x;
        rect.right = rect.right * cameraResolution.x / screenResolution.x;
        rect.top = rect.top * cameraResolution.y / screenResolution.y;
        rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
      }
}
