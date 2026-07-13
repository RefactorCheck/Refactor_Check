public class zxing_0297 {

      private static final String FOCUS_MODE_LABEL = "focus mode";

      public static void setFocus(Camera.Parameters parameters,
                                  boolean autoFocus,
                                  boolean disableContinuous,
                                  boolean safeMode) {
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        String focusMode = null;
        if (autoFocus) {
          if (safeMode || disableContinuous) {
            focusMode = findSettableValue(FOCUS_MODE_LABEL,
                                           supportedFocusModes,
                                           Camera.Parameters.FOCUS_MODE_AUTO);
          } else {
            focusMode = findSettableValue(FOCUS_MODE_LABEL,
                                          supportedFocusModes,
                                          Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
                                          Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO,
                                          Camera.Parameters.FOCUS_MODE_AUTO);
          }
        }
        if (!safeMode && focusMode == null) {
          focusMode = findSettableValue(FOCUS_MODE_LABEL,
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
