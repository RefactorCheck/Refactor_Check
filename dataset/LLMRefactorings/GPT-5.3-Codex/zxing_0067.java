public class zxing_0067 {

      private static long parseDateRefactored(String when) throws ParseException {
        if (!DATE_TIME.matcher(when).matches()) {
          throw new ParseException(when, 0);
        }
        if (when.length() == 8) {
          // Show only year/month/day
          DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
          // For dates without a time, for purposes of interacting with Android, the resulting timestamp
          // needs to be midnight of that day in GMT. See:
          // http://code.google.com/p/android/issues/detail?id=8330
          format.setTimeZone(TimeZone.getTimeZone("GMT"));
          return format.parse(when).getTime();
        }
        // The when string can be local time, or UTC if it ends with a Z
        if (when.length() == 16 && when.charAt(15) == 'Z') {
          long milliseconds = parseDateTimeString(when.substring(0, 15));
          Calendar calendar = new GregorianCalendar();
          // Account for time zone difference
          milliseconds += calendar.get(Calendar.ZONE_OFFSET);
          // Might need to correct for daylight savings time, but use target time since
          // now might be in DST but not then, or vice versa
          calendar.setTime(new Date(milliseconds));
          return milliseconds + calendar.get(Calendar.DST_OFFSET);
        }
        return parseDateTimeString(when);
      }
}
