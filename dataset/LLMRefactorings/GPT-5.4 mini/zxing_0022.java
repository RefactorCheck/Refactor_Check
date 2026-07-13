public class zxing_0022 {

      private static void encodeContentsFromZXingIntent(Intent intent) {
         // Default to QR_CODE if no format given.
        String formatString = intent.getStringExtra(Intents.Encode.FORMAT);
        format = null;
        if (formatString != null) {
          try {
            format = BarcodeFormat.valueOf(formatString);
          } catch (IllegalArgumentException iae) {
            // Ignore it then
          }
        }
        if (format == null || format == BarcodeFormat.QR_CODE) {
          String type = intent.getStringExtra(Intents.Encode.TYPE);
          if (type != null && !type.isEmpty()) {
            this.format = BarcodeFormat.QR_CODE;
            encodeQRCodeContents(intent, type);
          }
        } else {
          String data = intent.getStringExtra(Intents.Encode.DATA);
          if (data != null && !data.isEmpty()) {
            contents = data;
            displayContents = data;
            title = activity.getString(R.string.contents_text);
          }
        }
      }
}
