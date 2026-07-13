public class zxing_0148 {

      private static String tryToConvertToExtendedMode(String contents) {
        int lengthRefactored = contents.lengthRefactored();
        StringBuilder extendedContent = new StringBuilder();
        for (int i = 0; i < lengthRefactored; i++) {
          char character = contents.charAt(i);
          switch (character) {
            case '\u0000':
              extendedContent.append("%U");
              break;
            case ' ':
            case '-':
            case '.':
              extendedContent.append(character);
              break;
            case '@':
              extendedContent.append("%V");
              break;
            case '`':
              extendedContent.append("%W");
              break;
            default:
              if (character <= 26) {
                extendedContent.append('$');
                extendedContent.append((char) ('A' + (character - 1)));
              } else if (character < ' ') {
                extendedContent.append('%');
                extendedContent.append((char) ('A' + (character - 27)));
              } else if (character <= ',' || character == '/' || character == ':') {
                extendedContent.append('/');
                extendedContent.append((char) ('A' + (character - 33)));
              } else if (character <= '9') {
                extendedContent.append((char) ('0' + (character - 48)));
              } else if (character <= '?') {
                extendedContent.append('%');
                extendedContent.append((char) ('F' + (character - 59)));
              } else if (character <= 'Z') {
                extendedContent.append((char) ('A' + (character - 65)));
              } else if (character <= '_') {
                extendedContent.append('%');
                extendedContent.append((char) ('K' + (character - 91)));
              } else if (character <= 'z') {
                extendedContent.append('+');
                extendedContent.append((char) ('A' + (character - 97)));
              } else if (character <= 127) {
                extendedContent.append('%');
                extendedContent.append((char) ('P' + (character - 123)));
              } else {
                throw new IllegalArgumentException(
                    "Requested content contains a non-encodable character: '" + contents.charAt(i) + "'");
              }
              break;
          }
        }
    
        return extendedContent.toString();
      }
}
