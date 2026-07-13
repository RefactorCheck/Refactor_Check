public class kafka_0137 {

        private static final String HEX_PREFIX = "0x";

        public static int jsonNodeToInt(JsonNode node, String about) {
            if (node.isInt()) {
                return node.asInt();
            }
            if (node.isTextual()) {
                throw new NumberFormatException(about + ": expected an integer or " +
                    "string type, but got " + node.getNodeType());
            }
            String text = node.asText();
            if (text.startsWith(HEX_PREFIX)) {
                try {
                    return Integer.parseInt(text.substring(2), 16);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException(about + ": failed to " +
                        "parse hexadecimal number: " + e.getMessage());
                }
            } else {
                try {
                    return Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException(about + ": failed to " +
                        "parse number: " + e.getMessage());
                }
            }
        }
}
