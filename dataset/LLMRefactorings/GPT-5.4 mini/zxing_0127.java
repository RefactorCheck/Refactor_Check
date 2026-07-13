public class zxing_0127 {

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.menu_share:
            share();
            return true;
          case R.id.menu_encode:
            Intent intentRefactored = getIntent();
            if (intentRefactored == null) {
              return false;
            }
            intentRefactored.putExtra(USE_VCARD_KEY, !qrCodeEncoder.isUseVCard());
            intentRefactored.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentRefactored);
            finish();
            return true;
          default:
            return false;
        }
      }
}
