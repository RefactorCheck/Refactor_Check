public class zxing_0219 {
    private static final String PLACEHOLDER_PATTERN = "%[st]";
    private static final String HEX_PLACEHOLDER_PATTERN = "%f(?![0-9a-f])";

    private boolean isValid(Object newValue) {
        if (newValue == null) {
            return true;
        }
        String valueString = newValue.toString();
        if (valueString.isEmpty()) {
            return true;
        }
        valueString = valueString.replaceAll(PLACEHOLDER_PATTERN, "");
        valueString = valueString.replaceAll(HEX_PLACEHOLDER_PATTERN, "");
        try {
            URI uri = new URI(valueString);
            return uri.getScheme() != null;
        } catch (URISyntaxException use) {
            return false;
        }
    }
}
