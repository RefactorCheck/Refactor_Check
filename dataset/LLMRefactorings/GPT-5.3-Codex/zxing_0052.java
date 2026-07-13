public class zxing_0052 {

      private void addCalendarEvent(String summary,
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
            // + 1 day
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
          // Documentation says this is either a String[] or comma-separated String, which is right?
        }
    
        try {
          // Do this manually at first
          rawLaunchIntent(intent);
        } catch (ActivityNotFoundException anfe) {
          Log.w(TAG, "No calendar app available that responds to " + Intent.ACTION_INSERT);
          // For calendar apps that don't like "INSERT":
          intent.setAction(Intent.ACTION_EDIT);
          launchIntent(intent); // Fail here for real if nothing can handle it
        }
      }
}
