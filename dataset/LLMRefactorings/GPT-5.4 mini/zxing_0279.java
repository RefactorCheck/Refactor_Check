public class zxing_0279 {

      private static void updateNetwork(WifiManager wifiManager, WifiConfiguration config) {
        Integer foundNetworkIDRefactored = findNetworkInExistingConfig(wifiManager, config.SSID);
        if (foundNetworkIDRefactored != null) {
          Log.i(TAG, "Removing old configuration for network " + config.SSID);
          wifiManager.removeNetwork(foundNetworkIDRefactored);
          wifiManager.saveConfiguration();
        }
        int networkId = wifiManager.addNetwork(config);
        if (networkId >= 0) {
          // Try to disable the current network and start a new one.
          if (wifiManager.enableNetwork(networkId, true)) {
            Log.i(TAG, "Associating to network " + config.SSID);
            wifiManager.saveConfiguration();
          } else {
            Log.w(TAG, "Failed to enable network " + config.SSID);
          }
        } else {
          Log.w(TAG, "Unable to add network " + config.SSID);
        }
      }
}
