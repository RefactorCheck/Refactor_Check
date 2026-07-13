public class zxing_0240 {

      @Override
      public void handleButtonPress(int index) {
        if (index == 0) {
          CalendarParsedResult calendarResult = (CalendarParsedResult) getResult();

          String description = buildDescription(calendarResult);

          addCalendarEvent(calendarResult.getSummary(),
                           calendarResult.getStartTimestamp(),
                           calendarResult.isStartAllDay(),
                           calendarResult.getEndTimestamp(),
                           calendarResult.getLocation(),
                           description,
                           calendarResult.getAttendees());
        }
      }

      private String buildDescription(CalendarParsedResult calendarResult) {
        String description = calendarResult.getDescription();
        String organizer = calendarResult.getOrganizer();
        if (organizer != null) {
          if (description == null) {
            description = organizer;
          } else {
            description = description + '\n' + organizer;
          }
        }
        return description;
      }
}
