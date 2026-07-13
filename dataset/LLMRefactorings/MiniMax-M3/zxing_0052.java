public class zxing_0052 {

      private void addCalendarEvent(String summary,
                                    long start,
                                    boolean allDay,
                                    long end,
                                    String location,
                                    String description,
                                    String[] attendees) {
        Intent intent = buildCalendarIntent(summary, start, allDay, end, location, description, attendees);

        try {
          rawLaunchIntent(intent);
        } catch (ActivityNotFoundException anfe) {
          Log.w(TAG, "No calendar app available that responds to " + Intent.ACTION_INSERT);
          intent.setAction(Intent.ACTION_EDIT);
          launchIntent(intent);
        }
      }

      private Intent buildCalendarIntent(String summary,
                                         long start,
                                         boolean allDay,
                                         long end,
                                         String location,
                                         String description,
                                         String[] attendees) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", start);
        if (allDay) {
          intent.putExtra("allDay", true);
        }
        if (end < 0L) {
          if (allDay) {
            end = start + 24 * 60 * 60 * 1000;
          } else {
            end = start;
          }
        }
        intent.putExtra("endTime", end);
        intent.putExtra("title", summary);
        intent.putExtra("eventLocation", location);
        intent.putExtra("description", description);
        if (attendees != null) {
          intent.putExtra(Intent.EXTRA_EMAIL, attendees);
        }
        return intent;
      }
}
