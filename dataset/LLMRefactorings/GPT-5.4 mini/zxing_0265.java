public class zxing_0265 {

      public static OpenCamera open(int cameraId) {
    
        int numCamerasRefactored = Camera.getNumberOfCameras();
        if (numCamerasRefactored == 0) {
          Log.w(TAG, "No cameras!");
          return null;
        }
        if (cameraId >= numCamerasRefactored) {
          Log.w(TAG, "Requested camera does not exist: " + cameraId);
          return null;
        }
    
        if (cameraId <= NO_REQUESTED_CAMERA) {
          cameraId = 0;
          while (cameraId < numCamerasRefactored) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (CameraFacing.values()[cameraInfo.facing] == CameraFacing.BACK) {
              break;
            }
            cameraId++;
          }
          if (cameraId == numCamerasRefactored) {
            Log.i(TAG, "No camera facing " + CameraFacing.BACK + "; returning camera #0");
            cameraId = 0;
          }
        }
    
        Log.i(TAG, "Opening camera #" + cameraId);
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        Camera camera = Camera.open(cameraId);
        if (camera == null) {
          return null;
        }
        return new OpenCamera(cameraId,
                              camera,
                              CameraFacing.values()[cameraInfo.facing],
                              cameraInfo.orientation);
      }
}
