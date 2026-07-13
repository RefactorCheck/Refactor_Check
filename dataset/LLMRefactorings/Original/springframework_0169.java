public class springframework_0169 {

    	private TemporalAccessor doParse(String text, Locale locale, DateTimeFormatter formatter) throws DateTimeParseException {
    		DateTimeFormatter formatterToUse = DateTimeContextHolder.getFormatter(formatter, locale);
    		if (Instant.class == this.temporalAccessorType) {
    			return formatterToUse.parse(text, Instant::from);
    		}
    		else if (LocalDate.class == this.temporalAccessorType) {
    			return LocalDate.parse(text, formatterToUse);
    		}
    		else if (LocalTime.class == this.temporalAccessorType) {
    			return LocalTime.parse(text, formatterToUse);
    		}
    		else if (LocalDateTime.class == this.temporalAccessorType) {
    			return LocalDateTime.parse(text, formatterToUse);
    		}
    		else if (ZonedDateTime.class == this.temporalAccessorType) {
    			return ZonedDateTime.parse(text, formatterToUse);
    		}
    		else if (OffsetDateTime.class == this.temporalAccessorType) {
    			return OffsetDateTime.parse(text, formatterToUse);
    		}
    		else if (OffsetTime.class == this.temporalAccessorType) {
    			return OffsetTime.parse(text, formatterToUse);
    		}
    		else if (YearMonth.class == this.temporalAccessorType) {
    			return YearMonth.parse(text, formatterToUse);
    		}
    		else if (MonthDay.class == this.temporalAccessorType) {
    			return MonthDay.parse(text, formatterToUse);
    		}
    		else {
    			throw new IllegalStateException("Unsupported TemporalAccessor type: " + this.temporalAccessorType);
    		}
    	}
}
