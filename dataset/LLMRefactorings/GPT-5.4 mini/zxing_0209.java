public class zxing_0209 {
  private static final String EXTRACTED_CONSTANT_0209 = "No WifiManager available from device";


      @Override
      public void handleButtonPress(int index) {
        if (index == 0) {
          WifiParsedResult wifiResult = (WifiParsedResult) getResult();
          WifiManager wifiManager =
              (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
          if (wifiManager == null) {
            Log.w(TAG, EXTRACTED_CONSTANT_0209);
            return;
          }
          final Activity activity = getActivity();
          activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(activity.getApplicationContext(), R.string.wifi_changing_network, Toast.LENGTH_SHORT).show();
            }
          });
          new WifiConfigManager(wifiManager).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, wifiResult);
          parent.restartPreviewAfterDelay(0L);
        }
      }
}
