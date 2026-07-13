public class netty_0148 {

    static boolean containsCommaSeparatedTrimmed(CharSequence rawNext, CharSequence expected,
                                                     boolean ignoreCase) {
        int begin = 0;
        int end = AsciiString.indexOf(rawNext, ',', begin);
        if (end == -1) {
            return matches(trim(rawNext), expected, ignoreCase);
        }
        do {
            if (matches(trim(rawNext.subSequence(begin, end)), expected, ignoreCase)) {
                return true;
            }
            begin = end + 1;
        } while ((end = AsciiString.indexOf(rawNext, ',', begin)) != -1);

        if (begin < rawNext.length()) {
            return matches(trim(rawNext.subSequence(begin, rawNext.length())), expected, ignoreCase);
        }
        return false;
    }

    private static boolean matches(CharSequence trimmed, CharSequence expected, boolean ignoreCase) {
        return ignoreCase ? contentEqualsIgnoreCase(trimmed, expected) : contentEquals(trimmed, expected);
    }
}
