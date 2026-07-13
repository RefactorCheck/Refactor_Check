public class zxing_0050 {

    private static String findAIvalue(int i, String rawText) {
        char c = rawText.charAt(i);
        if (c != '(') {
            return null;
        }

        CharSequence rawTextAux = rawText.substring(i + 1);
        return parseAIValue(rawTextAux);
    }

    private static String parseAIValue(CharSequence rawTextAux) {
        StringBuilder buf = new StringBuilder();
        for (int index = 0; index < rawTextAux.length(); index++) {
            char currentChar = rawTextAux.charAt(index);
            if (currentChar == ')') {
                return buf.toString();
            }
            if (currentChar < '0' || currentChar > '9') {
                return null;
            }
            buf.append(currentChar);
        }
        return null;
    }
}
