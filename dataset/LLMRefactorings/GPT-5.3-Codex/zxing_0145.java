public class zxing_0145 {
static @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
          case KeyEvent.KEYCODE_BACK:
            if (source == IntentSource.NATIVE_APP_INTENT) {
              setResult(RESULT_CANCELED);
              finish();
              return true;
            }
            if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
              restartPreviewAfterDelay(0L);
              return true;
            }
            break;
          case KeyEvent.KEYCODE_FOCUS:
          case KeyEvent.KEYCODE_CAMERA:
            // Handle these events so they don't launch the Camera app
            return true;
          // Use volume up/down to turn on light
          case KeyEvent.KEYCODE_VOLUME_DOWN:
            cameraManager.setTorch(false);
            return true;
          case KeyEvent.KEYCODE_VOLUME_UP:
            cameraManager.setTorch(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
      }
}
