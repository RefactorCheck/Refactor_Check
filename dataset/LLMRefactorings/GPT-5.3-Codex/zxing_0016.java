public class zxing_0016 {

      @Override
      public CalendarParsedResult parse(Result result) {
        String rawText = getMassagedText(result);
        int vEventStart = rawText.indexOf("BEGIN:VEVENT");
        if (vEventStart < 0) {
          return null;
        }
    
        String summary = matchSingleVCardPrefixedField("SUMMARY", rawText);
        String start = matchSingleVCardPrefixedField("DTSTART", rawText);
        if (start == null) {
          return null;
        }
        String end = matchSingleVCardPrefixedField("DTEND", rawText);
        String duration = matchSingleVCardPrefixedField("DURATION", rawText);
        String location = matchSingleVCardPrefixedField("LOCATION", rawText);
        String organizer = stripMailto(matchSingleVCardPrefixedField("ORGANIZER", rawText));
    
        String[] attendees = matchVCardPrefixedField("ATTENDEE", rawText);
        if (attendees != null) {
          for (int i = 0; i < attendees.length; i++) {
            attendees[i] = stripMailto(attendees[i]);
          }
        }
        String description = matchSingleVCardPrefixedField("DESCRIPTION", rawText);
    
        String geoString = matchSingleVCardPrefixedField("GEO", rawText);
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
