public class zxing_0038 {

      CharSequence buildHistory() {
        StringBuilder historyText = new StringBuilder(1000);
        SQLiteOpenHelper helper = new DBHelper(activity);
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor cursor = db.query(DBHelper.TABLE_NAME,
                                      COLUMNS,
                                      null, null, null, null,
                                      DBHelper.TIMESTAMP_COL + " DESC")) {
          DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
          while (cursor.moveToNext()) {
            appendQuotedField(historyText, cursor.getString(0), ",");
            appendQuotedField(historyText, cursor.getString(1), ",");
            appendQuotedField(historyText, cursor.getString(2), ",");
            appendQuotedField(historyText, cursor.getString(3), ",");
    
            // Add timestamp again, formatted
            long timestamp = cursor.getLong(3);
            appendQuotedField(historyText, format.format(timestamp), ",");
    
            // Above we're preserving the old ordering of columns which had formatted data in position 5
    
            appendQuotedField(historyText, cursor.getString(4), "\r\n");
          }
        } catch (SQLException sqle) {
          Log.w(TAG, sqle);
        }
        return historyText;
      }
      
      private void appendQuotedField(StringBuilder historyText, String value, String suffix) {
        historyText.append('"').append(massageHistoryField(value)).append('"').append(suffix);
      }
}
