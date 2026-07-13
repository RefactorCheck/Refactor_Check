public class zxing_0297 {

      public static void setFocus(Camera.Parameters parameters,
                                  boolean autoFocus,
                                  boolean disableContinuous,
                                  boolean safeMode) {
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        String focusMode = null;
        if (autoFocus) {
          if (safeMode || disableContinuous) {
            focusMode = findSettableValue("focus mode",
                                           supportedFocusModes,
                                           Camera.Parameters.FOCUS_MODE_AUTO);
          } else {
            focusMode = findSettableValue("focus mode",
                                          supportedFocusModes,
                                          Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
                                          Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO,
                                          Camera.Parameters.FOCUS_MODE_AUTO);
          }
        }
        // Maybe selected auto-focus but not available, so fall through here:
        if (!safeMode && focusMode == null) {
          focusMode = findSettableValue("focus mode",
                                        supportedFocusModes,
                                        Camera.Parameters.FOCUS_MODE_MACRO,
                                        Camera.Parameters.FOCUS_MODE_EDOF);
        }
        if (focusMode != null) {
          if (focusMode.equals(parameters.getFocusMode())) {
            Log.i(TAG, "Focus mode already set to " + focusMode);
          } else {
            parameters.setFocusMode(focusMode);
          }
        }
      }
}
