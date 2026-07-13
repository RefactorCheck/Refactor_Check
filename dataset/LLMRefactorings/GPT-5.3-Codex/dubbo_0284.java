public class dubbo_0284 {

        private String getErrorUrl(final String code) {
    
            String trimmedString = code.trim();
    
            if (!ERROR_CODE_PATTERN.matcher(trimmedString).matches()) {
                error("Invalid error code: " + code + ", the format of error code is: X-X (where X is a number).");
                return "";
            }
    
            String[] segments = trimmedString.split("[-]");
    
            int[] errorCodeSegments = new int[2];
    
            try {
                errorCodeSegments[0] = Integer.parseInt(segments[0]);
                errorCodeSegments[1] = Integer.parseInt(segments[1]);
            } catch (NumberFormatException numberFormatException) {
                error(
                        "Invalid error code: " + code + ", the format of error code is: X-X (where X is a number).",
                        numberFormatException);
    
                return "";
            }
    
            return String.format(INSTRUCTIONS_URL, errorCodeSegments[0], errorCodeSegments[1]);
        }
}
