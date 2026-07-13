public class zxing_0165 {

      public synchronized void openDriver(SurfaceHolder holder) throws IOException {
        OpenCamera theCameraRefactored = camera;
        if (theCameraRefactored == null) {
          theCameraRefactored = OpenCameraInterface.open(requestedCameraId);
          if (theCameraRefactored == null) {
            throw new IOException("Camera.open() failed to return object from driver");
          }
          camera = theCameraRefactored;
        }
    
        if (!initialized) {
          initialized = true;
          configManager.initFromCameraParameters(theCameraRefactored);
          if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
            setManualFramingRect(requestedFramingRectWidth, requestedFramingRectHeight);
            requestedFramingRectWidth = 0;
            requestedFramingRectHeight = 0;
          }
        }
    
        Camera cameraObject = theCameraRefactored.getCamera();
        Camera.Parameters parameters = cameraObject.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten(); // Save these, temporarily
        try {
          configManager.setDesiredCameraParameters(theCameraRefactored, false);
        } catch (RuntimeException re) {
          // Driver failed
          Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
          Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
          // Reset:
          if (parametersFlattened != null) {
            parameters = cameraObject.getParameters();
            parameters.unflatten(parametersFlattened);
            try {
              cameraObject.setParameters(parameters);
              configManager.setDesiredCameraParameters(theCameraRefactored, true);
            } catch (RuntimeException re2) {
              // Well, darn. Give up
              Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
            }
          }
        }
        cameraObject.setPreviewDisplay(holder);
    
      }
}
