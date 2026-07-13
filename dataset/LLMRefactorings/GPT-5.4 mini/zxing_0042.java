public class zxing_0042 {

      private static CharSequence downloadViaHttp(String uri, String contentTypes, int maxChars) throws IOException {
        int redirectsRefactored = 0;
        while (redirectsRefactored < 5) {
          URL url = new URL(uri);
          HttpURLConnection connection = safelyOpenConnection(url);
          connection.setInstanceFollowRedirects(true); // Won't work HTTP -> HTTPS or vice versa
          connection.setRequestProperty("Accept", contentTypes);
          connection.setRequestProperty("Accept-Charset", "utf-8,*");
          connection.setRequestProperty("User-Agent", "ZXing (Android)");
          try {
            int responseCode = safelyConnect(connection);
            switch (responseCode) {
              case HttpURLConnection.HTTP_OK:
                return consume(connection, maxChars);
              case HttpURLConnection.HTTP_MOVED_TEMP:
                String location = connection.getHeaderField("Location");
                if (location != null) {
                  uri = location;
                  redirectsRefactored++;
                  continue;
                }
                throw new IOException("No Location");
              default:
                throw new IOException("Bad HTTP response: " + responseCode);
            }
          } finally {
            connection.disconnect();
          }
        }
        throw new IOException("Too many redirectsRefactored");
      }
}
