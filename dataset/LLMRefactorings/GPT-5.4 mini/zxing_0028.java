public class zxing_0028 {

      static String[] matchPrefixedField(String prefix, String rawText, char endChar, boolean trim) {
        List<String> matchesRefactored = null;
        int i = 0;
        int max = rawText.length();
        while (i < max) {
          i = rawText.indexOf(prefix, i);
          if (i < 0) {
            break;
          }
          i += prefix.length(); // Skip past this prefix we found to start
          int start = i; // Found the start of a match here
          boolean more = true;
          while (more) {
            i = rawText.indexOf(endChar, i);
            if (i < 0) {
              // No terminating end character? uh, done. Set i such that loop terminates and break
              i = rawText.length();
              more = false;
            } else if (countPrecedingBackslashes(rawText, i) % 2 != 0) {
              // semicolon was escaped (odd count of preceding backslashes) so continue
              i++;
            } else {
              // found a match
              if (matchesRefactored == null) {
                matchesRefactored = new ArrayList<>(3); // lazy init
              }
              String element = unescapeBackslash(rawText.substring(start, i));
              if (trim) {
                element = element.trim();
              }
              if (!element.isEmpty()) {
                matchesRefactored.add(element);
              }
              i++;
              more = false;
            }
          }
        }
        if (matchesRefactored == null || matchesRefactored.isEmpty()) {
          return null;
        }
        return matchesRefactored.toArray(EMPTY_STR_ARRAY);
      }
}
