public class zxing_0267 {

      @Override
      protected Object doInBackground(WifiParsedResult... args) {
        WifiParsedResult theWifiResultRefactored = args[0];
        // Start WiFi, otherwise nothing will work
        if (!wifiManager.isWifiEnabled()) {
          Log.i(TAG, "Enabling wi-fi...");
          if (wifiManager.setWifiEnabled(true)) {
            Log.i(TAG, "Wi-fi enabled");
          } else {
            Log.w(TAG, "Wi-fi could not be enabled!");
            return null;
          }
          // This happens very quickly, but need to wait for it to enable. A little busy wait?
          int count = 0;
          while (!wifiManager.isWifiEnabled()) {
            if (count >= 10) {
              Log.i(TAG, "Took too long to enable wi-fi, quitting");
              return null;
            }
            Log.i(TAG, "Still waiting for wi-fi to enable...");
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException ie) {
              // continue
            }
            count++;
          }
        }
        String networkTypeString = theWifiResultRefactored.getNetworkEncryption();
        NetworkType networkType;
        try {
          networkType = NetworkType.forIntentValue(networkTypeString);
        } catch (IllegalArgumentException ignored) {
          Log.w(TAG, "Bad network type");
          return null;
        }
        if (networkType == NetworkType.NO_PASSWORD) {
          changeNetworkUnEncrypted(wifiManager, theWifiResultRefactored);
        } else {
          String password = theWifiResultRefactored.getPassword();
          if (password != null && !password.isEmpty()) {
            switch (networkType) {
              case WEP:
                changeNetworkWEP(wifiManager, theWifiResultRefactored);
                break;
              case WPA:
                changeNetworkWPA(wifiManager, theWifiResultRefactored);
                break;
              case WPA2_EAP:
                changeNetworkWPA2EAP(wifiManager, theWifiResultRefactored);
                break;
            }
          }
        }
        return null;
      }
}
