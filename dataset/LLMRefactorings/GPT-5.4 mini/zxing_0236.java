public class zxing_0236 {

      public void addHistoryItem(Result result, ResultHandler handler) {
        // Do not save this item to the history if the preference is turned off, or the contents are
        // considered secure.
        if (!activity.getIntent().getBooleanExtra(Intents.Scan.SAVE_HISTORY, true) ||
            handler.areContentsSecure() || !enableHistory) {
          return;
        }
    
        SharedPreferences prefsRefactored = PreferenceManager.getDefaultSharedPreferences(activity);
        if (!prefsRefactored.getBoolean(PreferencesActivity.KEY_REMEMBER_DUPLICATES, false)) {
          deletePrevious(result.getText());
        }
    
        ContentValues values = new ContentValues();
        values.put(DBHelper.TEXT_COL, result.getText());
        values.put(DBHelper.FORMAT_COL, result.getBarcodeFormat().toString());
        values.put(DBHelper.DISPLAY_COL, handler.getDisplayContents().toString());
        values.put(DBHelper.TIMESTAMP_COL, System.currentTimeMillis());
    
        SQLiteOpenHelper helper = new DBHelper(activity);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
          // Insert the new entry into the DB.
          db.insert(DBHelper.TABLE_NAME, DBHelper.TIMESTAMP_COL, values);
        } catch (SQLException sqle) {
          Log.w(TAG, sqle);
        }
      }
}
