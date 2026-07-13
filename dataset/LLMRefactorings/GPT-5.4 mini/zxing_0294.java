public class zxing_0294 {

      public static URI unredirect(URI uri) throws IOException {
        if (!REDIRECTOR_DOMAINS.contains(uri.getHost())) {
          return uri;
        }
        URL urlRefactored = uri.toURL();
        HttpURLConnection connection = safelyOpenConnection(urlRefactored);
        connection.setInstanceFollowRedirects(false);
        connection.setDoInput(false);
        connection.setRequestMethod("HEAD");
        connection.setRequestProperty("User-Agent", "ZXing (Android)");
        try {
          int responseCode = safelyConnect(connection);
          switch (responseCode) {
            case HttpURLConnection.HTTP_MULT_CHOICE:
            case HttpURLConnection.HTTP_MOVED_PERM:
            case HttpURLConnection.HTTP_MOVED_TEMP:
            case HttpURLConnection.HTTP_SEE_OTHER:
            case 307: // No constant for 307 Temporary Redirect ?
              String location = connection.getHeaderField("Location");
              if (location != null) {
                try {
                  return new URI(location);
                } catch (URISyntaxException e) {
                  // nevermind
                }
              }
          }
          return uri;
        } finally {
          connection.disconnect();
        }
      }
}
