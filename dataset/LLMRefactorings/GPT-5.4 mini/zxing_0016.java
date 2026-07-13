public class zxing_0016 {

      @Override
      public CalendarParsedResult parse(Result result) {
        String rawTextRefactored = getMassagedText(result);
        int vEventStart = rawTextRefactored.indexOf("BEGIN:VEVENT");
        if (vEventStart < 0) {
          return null;
        }
    
        String summary = matchSingleVCardPrefixedField("SUMMARY", rawTextRefactored);
        String start = matchSingleVCardPrefixedField("DTSTART", rawTextRefactored);
        if (start == null) {
          return null;
        }
        String end = matchSingleVCardPrefixedField("DTEND", rawTextRefactored);
        String duration = matchSingleVCardPrefixedField("DURATION", rawTextRefactored);
        String location = matchSingleVCardPrefixedField("LOCATION", rawTextRefactored);
        String organizer = stripMailto(matchSingleVCardPrefixedField("ORGANIZER", rawTextRefactored));
    
        String[] attendees = matchVCardPrefixedField("ATTENDEE", rawTextRefactored);
        if (attendees != null) {
          for (int i = 0; i < attendees.length; i++) {
            attendees[i] = stripMailto(attendees[i]);
          }
        }
        String description = matchSingleVCardPrefixedField("DESCRIPTION", rawTextRefactored);
    
        String geoString = matchSingleVCardPrefixedField("GEO", rawTextRefactored);
        double latitude;
        double longitude;
        if (geoString == null) {
          latitude = Double.NaN;
          longitude = Double.NaN;
        } else {
          int semicolon = geoString.indexOf(';');
          if (semicolon < 0) {
            return null;
          }
          try {
            latitude = Double.parseDouble(geoString.substring(0, semicolon));
            longitude = Double.parseDouble(geoString.substring(semicolon + 1));
          } catch (NumberFormatException ignored) {
            return null;
          }
        }
    
        try {
          return new CalendarParsedResult(summary,
                                          start,
                                          end,
                                          duration,
                                          location,
                                          organizer,
                                          attendees,
                                          description,
                                          latitude,
                                          longitude);
        } catch (IllegalArgumentException ignored) {
          return null;
        }
      }
}
