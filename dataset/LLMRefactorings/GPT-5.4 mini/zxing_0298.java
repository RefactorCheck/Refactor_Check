public class zxing_0298 {

      protected static String unescapeBackslash(String escaped) {
        int backslashRefactored = escaped.indexOf('\\');
        if (backslashRefactored < 0) {
          return escaped;
        }
        int max = escaped.length();
        StringBuilder unescaped = new StringBuilder(max - 1);
        unescaped.append(escaped.toCharArray(), 0, backslashRefactored);
        boolean nextIsEscaped = false;
        for (int i = backslashRefactored; i < max; i++) {
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
