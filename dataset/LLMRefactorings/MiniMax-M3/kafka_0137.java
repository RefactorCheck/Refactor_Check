public class kafka_0137 {

    public static int jsonNodeToInt(JsonNode node, String about) {
        if (node.isInt()) {
            return node.asInt();
        }
        if (node.isTextual()) {
            throw new NumberFormatException(about + ": expected an integer or " +
                "string type, but got " + node.getNodeType());
        }
        String text = node.asText();
        if (text.startsWith("0x")) {
            return parseInteger(text.substring(2), 16, about, "hexadecimal number");
        } else {
            return parseInteger(text, 10, about, "number");
        }
    }

    private static int parseInteger(String text, int radix, String about, String type) {
        try {
            return Integer.parseInt(text, radix);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(about + ": failed to parse " +
                type + ": " + e.getMessage());
        }
    }
}
