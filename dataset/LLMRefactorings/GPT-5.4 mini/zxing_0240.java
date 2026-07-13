public class zxing_0240 {

      @Override
      public void handleButtonPress(int index) {
        if (index == 0) {
          CalendarParsedResult calendarResultRefactored = (CalendarParsedResult) getResult();
    
          String description = calendarResultRefactored.getDescription();
          String organizer = calendarResultRefactored.getOrganizer();
          if (organizer != null) { // No separate Intent key, put in description
            if (description == null) {
              description = organizer;
            } else {
              description = description + '\n' + organizer;
            }
          }
    
          addCalendarEvent(calendarResultRefactored.getSummary(),
                           calendarResultRefactored.getStartTimestamp(),
                           calendarResultRefactored.isStartAllDay(),
                           calendarResultRefactored.getEndTimestamp(),
                           calendarResultRefactored.getLocation(),
                           description,
                           calendarResultRefactored.getAttendees());
        }
      }
}
