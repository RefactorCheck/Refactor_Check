public class zxing_0165 {

      public synchronized void openDriver(SurfaceHolder holder) throws IOException {
        OpenCamera theCamera = camera;
        if (theCamera == null) {
          theCamera = OpenCameraInterface.open(requestedCameraId);
          if (theCamera == null) {
            throw new IOException("Camera.open() failed to return object from driver");
          }
          camera = theCamera;
        }
    
        if (!initialized) {
          initialized = true;
          configManager.initFromCameraParameters(theCamera);
          if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
            setManualFramingRect(requestedFramingRectWidth, requestedFramingRectHeight);
            requestedFramingRectWidth = 0;
            requestedFramingRectHeight = 0;
          }
        }
    
        Camera cameraObject = theCamera.getCamera();
        Camera.Parameters parameters = cameraObject.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten();
        try {
          configManager.setDesiredCameraParameters(theCamera, false);
        } catch (RuntimeException re) {
          Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
          Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
          resetCameraParameters(cameraObject, theCamera, parametersFlattened);
        }
        cameraObject.setPreviewDisplay(holder);
      }

      private void resetCameraParameters(Camera cameraObject, OpenCamera theCamera, String parametersFlattened) {
        if (parametersFlattened != null) {
          Camera.Parameters parameters = cameraObject.getParameters();
          parameters.unflatten(parametersFlattened);
          try {
            cameraObject.setParameters(parameters);
            configManager.setDesiredCameraParameters(theCamera, true);
          } catch (RuntimeException re2) {
            Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
          }
        }
      }
}
