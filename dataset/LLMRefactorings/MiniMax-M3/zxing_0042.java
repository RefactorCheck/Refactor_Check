public class zxing_0042 {

      private static final int MAX_REDIRECTS = 5;
      private static final String HEADER_ACCEPT = "Accept";
      private static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
      private static final String HEADER_USER_AGENT = "User-Agent";
      private static final String USER_AGENT_VALUE = "ZXing (Android)";
      private static final String CHARSET_VALUE = "utf-8,*";
      private static final String HEADER_LOCATION = "Location";
      private static final String ERROR_NO_LOCATION = "No Location";
      private static final String ERROR_BAD_RESPONSE_PREFIX = "Bad HTTP response: ";
      private static final String ERROR_TOO_MANY_REDIRECTS = "Too many redirects";

      private static CharSequence downloadViaHttp(String uri, String contentTypes, int maxChars) throws IOException {
        int redirects = 0;
        while (redirects < MAX_REDIRECTS) {
          URL url = new URL(uri);
          HttpURLConnection connection = safelyOpenConnection(url);
          connection.setInstanceFollowRedirects(true); // Won't work HTTP -> HTTPS or vice versa
          connection.setRequestProperty(HEADER_ACCEPT, contentTypes);
          connection.setRequestProperty(HEADER_ACCEPT_CHARSET, CHARSET_VALUE);
          connection.setRequestProperty(HEADER_USER_AGENT, USER_AGENT_VALUE);
          try {
            int responseCode = safelyConnect(connection);
            switch (responseCode) {
              case HttpURLConnection.HTTP_OK:
                return consume(connection, maxChars);
              case HttpURLConnection.HTTP_MOVED_TEMP:
                String location = connection.getHeaderField(HEADER_LOCATION);
                if (location != null) {
                  uri = location;
                  redirects++;
                  continue;
                }
                throw new IOException(ERROR_NO_LOCATION);
              default:
                throw new IOException(ERROR_BAD_RESPONSE_PREFIX + responseCode);
            }
          } finally {
            connection.disconnect();
          }
        }
        throw new IOException(ERROR_TOO_MANY_REDIRECTS);
      }
}
