public class zxing_0127 {

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.menu_share:
            share();
            return true;
          case R.id.menu_encode:
            Intent intent = getIntent();
            if (intent == null) {
              return false;
            }
            intent.putExtra(USE_VCARD_KEY, !qrCodeEncoder.isUseVCard());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
          default:
            return false;
        }
      }
}
