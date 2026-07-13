public class zxing_0035 {

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intents.FLAG_NEW_DOC);
        switch (item.getItemId()) {
          case R.id.menu_share:
            intent.setClassName(this, ShareActivity.class.getName());
            startActivity(intent);
            break;
          case R.id.menu_history:
            intent.setClassName(this, HistoryActivity.class.getName());
            startActivityForResult(intent, HISTORY_REQUEST_CODE);
            break;
          case R.id.menu_settings:
            intent.setClassName(this, PreferencesActivity.class.getName());
            startActivity(intent);
            break;
          case R.id.menu_help:
            intent.setClassName(this, HelpActivity.class.getName());
            startActivity(intent);
            break;
          default:
            return super.onOptionsItemSelected(item);
        }
        return true;
      }
}
