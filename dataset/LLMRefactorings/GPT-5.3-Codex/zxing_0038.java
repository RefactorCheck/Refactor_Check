public class zxing_0038 {
static CharSequence buildHistory() {
        StringBuilder historyText = new StringBuilder(1000);
        SQLiteOpenHelper helper = new DBHelper(activity);
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor cursor = db.query(DBHelper.TABLE_NAME,
                                      COLUMNS,
                                      null, null, null, null,
                                      DBHelper.TIMESTAMP_COL + " DESC")) {
          DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
          while (cursor.moveToNext()) {
    
            historyText.append('"').append(massageHistoryField(cursor.getString(0))).append("\",");
            historyText.append('"').append(massageHistoryField(cursor.getString(1))).append("\",");
            historyText.append('"').append(massageHistoryField(cursor.getString(2))).append("\",");
            historyText.append('"').append(massageHistoryField(cursor.getString(3))).append("\",");
    
            // Add timestamp again, formatted
            long timestamp = cursor.getLong(3);
            historyText.append('"').append(massageHistoryField(format.format(timestamp))).append("\",");
    
            // Above we're preserving the old ordering of columns which had formatted data in position 5
    
            historyText.append('"').append(massageHistoryField(cursor.getString(4))).append("\"\r\n");
          }
        } catch (SQLException sqle) {
          Log.w(TAG, sqle);
        }
        return historyText;
      }
}
