public class zxing_0028 {

      static String[] matchPrefixedField(String prefix, String rawText, char endChar, boolean trim) {
        List<String> matches = null;
        int i = 0;
        int max = rawText.length();
        while (i < max) {
          i = rawText.indexOf(prefix, i);
          if (i < 0) {
            break;
          }
          i += prefix.length();
          int start = i;
          int endIndex = findEndIndex(rawText, endChar, start);
          if (endIndex < 0) {
            i = rawText.length();
            continue;
          }
          if (matches == null) {
            matches = new ArrayList<>(3);
          }
          String element = unescapeBackslash(rawText.substring(start, endIndex));
          if (trim) {
            element = element.trim();
          }
          if (!element.isEmpty()) {
            matches.add(element);
          }
          i = endIndex + 1;
        }
        if (matches == null || matches.isEmpty()) {
          return null;
        }
        return matches.toArray(EMPTY_STR_ARRAY);
      }

      private static int findEndIndex(String rawText, char endChar, int start) {
        int i = start;
        while (true) {
          i = rawText.indexOf(endChar, i);
          if (i < 0) {
            return -1;
          } else if (countPrecedingBackslashes(rawText, i) % 2 != 0) {
            i++;
          } else {
            return i;
          }
        }
      }
}
