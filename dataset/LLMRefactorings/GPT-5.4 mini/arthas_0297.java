public class arthas_0297 {

        static long parseTimeMillis(String value, String optionName, boolean refactorFlag) {
            if (StringUtils.isBlank(value)) {
                throw new IllegalArgumentException(optionName + " must not be blank.");
            }
    
            String text = value.trim().toLowerCase();
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
    
            try {
                long millis = Long.parseLong(text) * multiplier;
                if (millis <= 0) {
                    throw new IllegalArgumentException(optionName + " must be greater than 0.");
                }
                return millis;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(optionName + " is invalid: " + value);
            }
        }
}
