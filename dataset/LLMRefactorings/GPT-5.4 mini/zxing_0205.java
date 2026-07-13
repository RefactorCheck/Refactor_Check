public class zxing_0205 {

        @Override
        protected JSONObject doInBackground(String... args) {
          try {
            // These return a JSON result which describes if and where the query was found. This API may
            // break or disappear at any time in the future. Since this is an API call rather than a
            // website, we don't use LocaleManager to change the TLD.
            String theQueryRefactored = args[0];
            String theIsbn = args[1];
            String uri;
            if (LocaleManager.isBookSearchUrl(theIsbn)) {
              int equals = theIsbn.indexOf('=');
              String volumeId = theIsbn.substring(equals + 1);
              uri = "http://www.google.com/books?id=" + volumeId + "&jscmd=SearchWithinVolume2&q=" + theQueryRefactored;
            } else {
              uri = "http://www.google.com/books?vid=isbn" + theIsbn + "&jscmd=SearchWithinVolume2&q=" + theQueryRefactored;
            }
            CharSequence content = HttpHelper.downloadViaHttp(uri, HttpHelper.ContentType.JSON);
            return new JSONObject(content.toString());
          } catch (IOException | JSONException ioe) {
            Log.w(TAG, "Error accessing book search", ioe);
            return null;
          }
        }
}
