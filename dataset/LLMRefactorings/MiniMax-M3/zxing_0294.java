public class zxing_0294 {

      public static URI unredirect(URI uri) throws IOException {
        if (!REDIRECTOR_DOMAINS.contains(uri.getHost())) {
          return uri;
        }
        URL url = uri.toURL();
        HttpURLConnection connection = safelyOpenConnection(url);
        connection.setInstanceFollowRedirects(false);
        connection.setDoInput(false);
        connection.setRequestMethod("HEAD");
        connection.setRequestProperty("User-Agent", "ZXing (Android)");
        try {
          int responseCode = safelyConnect(connection);
          return handleRedirectResponse(connection, responseCode, uri);
        } finally {
          connection.disconnect();
        }
      }

      private static URI handleRedirectResponse(HttpURLConnection connection, int responseCode, URI originalUri) {
        switch (responseCode) {
          case HttpURLConnection.HTTP_MULT_CHOICE:
          case HttpURLConnection.HTTP_MOVED_PERM:
          case HttpURLConnection.HTTP_MOVED_TEMP:
          case HttpURLConnection.HTTP_SEE_OTHER:
          case 307:
            String location = connection.getHeaderField("Location");
            if (location != null) {
              try {
                return new URI(location);
              } catch (URISyntaxException e) {
              }
            }
        }
        return originalUri;
      }
}
