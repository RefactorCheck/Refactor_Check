public class zxing_0240 {

      @Override
      public void handleButtonPress(int index) {
        if (index == 0) {
          CalendarParsedResult calendarResult = (CalendarParsedResult) getResult();
    
          String description = calendarResult.getDescription();
          String organizer = calendarResult.getOrganizer();
          if (organizer != null) { // No separate Intent key, put in description
            if (description == null) {
              description = organizer;
            } else {
              description = description + '\n' + organizer;
            }
          }
    
          addCalendarEvent(calendarResult.getSummary(),
                           calendarResult.getStartTimestamp(),
                           calendarResult.isStartAllDay(),
                           calendarResult.getEndTimestamp(),
                           calendarResult.getLocation(),
                           description,
                           calendarResult.getAttendees());
        }
      }
}
