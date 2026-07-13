public class zxing_0260 {

      public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();
        lastResult = rawResult;
        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
    
        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
          historyManager.addHistoryItem(rawResult, resultHandler);
          // Then not from history, so beep/vibrate and we have an image to draw on
          beepManager.playBeepSoundAndVibrate();
          drawResultPoints(barcode, scaleFactor, rawResult);
        }
    
        switch (source) {
          case NATIVE_APP_INTENT:
          case PRODUCT_SEARCH_LINK:
            handleDecodeExternally(rawResult, resultHandler, barcode);
            break;
          case ZXING_LINK:
            if (scanFromWebPageManager == null || !scanFromWebPageManager.isScanFromWebPage()) {
              handleDecodeInternally(rawResult, resultHandler, barcode);
            } else {
              handleDecodeExternally(rawResult, resultHandler, barcode);
            }
            break;
          case NONE:
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (fromLiveScan && prefs.getBoolean(PreferencesActivity.KEY_BULK_MODE, false)) {
              Toast.makeText(getApplicationContext(),
                             getResources().getString(R.string.msg_bulk_mode_scanned) + " (" + rawResult.getText() + ')',
                             Toast.LENGTH_SHORT).show();
              maybeSetClipboard(resultHandler);
              // Wait a moment or else it will scan the same barcode continuously about 3 times
              restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
            } else {
              handleDecodeInternally(rawResult, resultHandler, barcode);
            }
            break;
        }
      }
}
