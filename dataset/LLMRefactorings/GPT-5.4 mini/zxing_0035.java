public class zxing_0035 {

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
        Intent intentRefactored = new Intent(Intent.ACTION_VIEW);
        intentRefactored.addFlags(Intents.FLAG_NEW_DOC);
        switch (item.getItemId()) {
          case R.id.menu_share:
            intentRefactored.setClassName(this, ShareActivity.class.getName());
            startActivity(intentRefactored);
            break;
          case R.id.menu_history:
            intentRefactored.setClassName(this, HistoryActivity.class.getName());
            startActivityForResult(intentRefactored, HISTORY_REQUEST_CODE);
            break;
          case R.id.menu_settings:
            intentRefactored.setClassName(this, PreferencesActivity.class.getName());
            startActivity(intentRefactored);
            break;
          case R.id.menu_help:
            intentRefactored.setClassName(this, HelpActivity.class.getName());
            startActivity(intentRefactored);
            break;
          default:
            return super.onOptionsItemSelected(item);
        }
        return true;
      }
}
