public class zxing_0035 {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intents.FLAG_NEW_DOC);
        switch (item.getItemId()) {
            case R.id.menu_share:
                launchActivity(intent, ShareActivity.class);
                break;
            case R.id.menu_history:
                launchActivityForResult(intent, HistoryActivity.class, HISTORY_REQUEST_CODE);
                break;
            case R.id.menu_settings:
                launchActivity(intent, PreferencesActivity.class);
                break;
            case R.id.menu_help:
                launchActivity(intent, HelpActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void launchActivity(Intent intent, Class<?> activityClass) {
        intent.setClassName(this, activityClass.getName());
        startActivity(intent);
    }

    private void launchActivityForResult(Intent intent, Class<?> activityClass, int requestCode) {
        intent.setClassName(this, activityClass.getName());
        startActivityForResult(intent, requestCode);
    }
}
