public class zxing_0101 {

      private void encodeFromTextExtras(Intent intent) throws WriterException {
        // Notice: Google Maps shares both URL and details in one text, bummer!
        String theContents = ContactEncoder.trim(intent.getStringExtra(Intent.EXTRA_TEXT));
        if (theContents == null) {
          theContents = ContactEncoder.trim(intent.getStringExtra("android.intent.extra.HTML_TEXT"));
          // Intent.EXTRA_HTML_TEXT
          if (theContents == null) {
            theContents = ContactEncoder.trim(intent.getStringExtra(Intent.EXTRA_SUBJECT));
            if (theContents == null) {
              String[] emails = intent.getStringArrayExtra(Intent.EXTRA_EMAIL);
              if (emails != null) {
                theContents = ContactEncoder.trim(emails[0]);
              } else {
                theContents = "?";
              }
            }
          }
        }
    
        // Trim text to avoid URL breaking.
        if (theContents == null || theContents.isEmpty()) {
          throw new WriterException("Empty EXTRA_TEXT");
        }
        contents = theContents;
        // We only do QR code.
        format = BarcodeFormat.QR_CODE;
        displayContents = resolveDisplayContents(intent);
        title = activity.getString(R.string.contents_text);
      }

      private String resolveDisplayContents(Intent intent) {
        if (intent.hasExtra(Intent.EXTRA_SUBJECT)) {
          return intent.getStringExtra(Intent.EXTRA_SUBJECT);
        } else if (intent.hasExtra(Intent.EXTRA_TITLE)) {
          return intent.getStringExtra(Intent.EXTRA_TITLE);
        } else {
          return contents;
        }
      }
}
