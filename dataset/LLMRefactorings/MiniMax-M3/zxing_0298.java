public class zxing_0298 {

      protected static String unescapeBackslash(String escaped) {
        int backslash = escaped.indexOf('\\');
        if (backslash < 0) {
          return escaped;
        }
        int max = escaped.length();
        StringBuilder unescaped = new StringBuilder(max - 1);
        unescaped.append(escaped.toCharArray(), 0, backslash);
        processCharacters(escaped, backslash, max, unescaped);
        return unescaped.toString();
      }

      private static void processCharacters(String escaped, int start, int end, StringBuilder unescaped) {
        boolean nextIsEscaped = false;
        for (int i = start; i < end; i++) {
          char c = escaped.charAt(i);
          if (nextIsEscaped || c != '\\') {
            unescaped.append(c);
            nextIsEscaped = false;
          } else {
            nextIsEscaped = true;
          }
        }
      }
}
