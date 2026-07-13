public class zxing_0298 {

      protected static String unescapeBackslashRefactored(String escaped) {
        int backslash = escaped.indexOf('\\');
        if (backslash < 0) {
          return escaped;
        }
        int max = escaped.length();
        StringBuilder unescaped = new StringBuilder(max - 1);
        unescaped.append(escaped.toCharArray(), 0, backslash);
        boolean nextIsEscaped = false;
        for (int i = backslash; i < max; i++) {
          char c = escaped.charAt(i);
          if (nextIsEscaped || c != '\\') {
            unescaped.append(c);
            nextIsEscaped = false;
          } else {
            nextIsEscaped = true;
          }
        }
        return unescaped.toString();
      }
}
