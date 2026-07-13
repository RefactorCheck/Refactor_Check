public class springframework_0169 {

    private TemporalAccessor doParse(String text, Locale locale, DateTimeFormatter formatter) throws DateTimeParseException {
        DateTimeFormatter formatterToUse = DateTimeContextHolder.getFormatter(formatter, locale);
        Class<?> targetType = this.temporalAccessorType;
        if (Instant.class == targetType) {
            return formatterToUse.parse(text, Instant::from);
        }
        else if (LocalDate.class == targetType) {
            return LocalDate.parse(text, formatterToUse);
        }
        else if (LocalTime.class == targetType) {
            return LocalTime.parse(text, formatterToUse);
        }
        else if (LocalDateTime.class == targetType) {
            return LocalDateTime.parse(text, formatterToUse);
        }
        else if (ZonedDateTime.class == targetType) {
            return ZonedDateTime.parse(text, formatterToUse);
        }
        else if (OffsetDateTime.class == targetType) {
            return OffsetDateTime.parse(text, formatterToUse);
        }
        else if (OffsetTime.class == targetType) {
            return OffsetTime.parse(text, formatterToUse);
        }
        else if (YearMonth.class == targetType) {
            return YearMonth.parse(text, formatterToUse);
        }
        else if (MonthDay.class == targetType) {
            return MonthDay.parse(text, formatterToUse);
        }
        else {
            throw new IllegalStateException("Unsupported TemporalAccessor type: " + targetType);
        }
    }
}
