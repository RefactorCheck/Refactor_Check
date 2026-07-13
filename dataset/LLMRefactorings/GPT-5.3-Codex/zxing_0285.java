public class zxing_0285 {
private static final String DEFAULT_STRING = "Could not encode barcode";


      @Override
      protected void onResume() {
        super.onResume();
        // This assumes the view is full screen, which is a good assumption
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        int width = displaySize.x;
        int height = displaySize.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 7 / 8;
    
        Intent intent = getIntent();
        if (intent == null) {
          return;
        }
    
        try {
          boolean useVCard = intent.getBooleanExtra(USE_VCARD_KEY, false);
          qrCodeEncoder = new QRCodeEncoder(this, intent, smallerDimension, useVCard);
          Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
          if (bitmap == null) {
            Log.w(TAG, DEFAULT_STRING);
            showErrorMessage(R.string.msg_encode_contents_failed);
            qrCodeEncoder = null;
            return;
          }
    
          ImageView view = (ImageView) findViewById(R.id.image_view);
          view.setImageBitmap(bitmap);
    
          TextView contents = (TextView) findViewById(R.id.contents_text_view);
          if (intent.getBooleanExtra(Intents.Encode.SHOW_CONTENTS, true)) {
            contents.setText(qrCodeEncoder.getDisplayContents());
            setTitle(qrCodeEncoder.getTitle());
          } else {
            contents.setText("");
            setTitle("");
          }
        } catch (WriterException e) {
          Log.w(TAG, "Could not encode barcode", e);
          showErrorMessage(R.string.msg_encode_contents_failed);
          qrCodeEncoder = null;
        }
      }
}
