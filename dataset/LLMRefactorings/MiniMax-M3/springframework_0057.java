public class springframework_0057 {

    private long parseDateValue(@Nullable String headerValue) {
        if (headerValue == null) {
            return -1;
        }
        if (headerValue.length() >= 3) {
            return tryParseDateWithFormats(headerValue);
        }
        return -1;
    }

    private long tryParseDateWithFormats(String headerValue) {
        for (String dateFormat : DATE_FORMATS) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
            simpleDateFormat.setTimeZone(GMT);
            try {
                return simpleDateFormat.parse(headerValue).getTime();
            }
            catch (ParseException ex) {
                // ignore
            }
        }
        return -1;
    }
}
