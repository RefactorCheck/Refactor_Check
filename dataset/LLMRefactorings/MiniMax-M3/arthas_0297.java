public class arthas_0297 {

    static long parseTimeMillis(String value, String optionName) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(optionName + " must not be blank.");
        }

        ParsedTime parsed = parseTimeUnit(value.trim().toLowerCase());
        try {
            long millis = Long.parseLong(parsed.text) * parsed.multiplier;
            if (millis <= 0) {
                throw new IllegalArgumentException(optionName + " must be greater than 0.");
            }
            return millis;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(optionName + " is invalid: " + value);
        }
    }

    private static ParsedTime parseTimeUnit(String text) {
        long multiplier = 1;
        if (text.endsWith("ms")) {
            text = text.substring(0, text.length() - 2).trim();
        } else if (text.endsWith("s")) {
            text = text.substring(0, text.length() - 1).trim();
            multiplier = 1000;
        } else if (text.endsWith("m")) {
            text = text.substring(0, text.length() - 1).trim();
            multiplier = 60 * 1000;
        }
        return new ParsedTime(text, multiplier);
    }

    private static class ParsedTime {
        final String text;
        final long multiplier;

        ParsedTime(String text, long multiplier) {
            this.text = text;
            this.multiplier = multiplier;
        }
    }
}
