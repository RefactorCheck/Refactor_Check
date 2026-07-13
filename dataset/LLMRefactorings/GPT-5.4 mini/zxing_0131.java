public class zxing_0131 {
  private static final String EXTRACTED_CONSTANT_0131 = "No SurfaceHolder provided";


      private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
          throw new IllegalStateException(EXTRACTED_CONSTANT_0131);
        }
        if (cameraManager.isOpen()) {
          Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
          return;
        }
        try {
          cameraManager.openDriver(surfaceHolder);
          // Creating the handler starts the preview, which can also throw a RuntimeException.
          if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
          }
          decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
          Log.w(TAG, ioe);
          displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
          // Barcode Scanner has seen crashes in the wild of this variety:
          // java.?lang.?RuntimeException: Fail to connect to camera service
          Log.w(TAG, "Unexpected error initializing camera", e);
          displayFrameworkBugMessageAndExit();
        }
      }
}
