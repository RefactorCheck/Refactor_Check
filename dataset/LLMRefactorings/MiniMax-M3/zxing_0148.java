public class zxing_0148 {

      private static String tryToConvertToExtendedMode(String contents) {
        int length = contents.length();
        StringBuilder extendedContent = new StringBuilder();
        for (int i = 0; i < length; i++) {
          char character = contents.charAt(i);
          extendedContent.append(convertChar(character, contents, i));
        }
    
        return extendedContent.toString();
      }

      private static String convertChar(char character, String contents, int i) {
        switch (character) {
            case '\u0000':
              return "%U";
            case ' ':
            case '-':
            case '.':
              return String.valueOf(character);
            case '@':
              return "%V";
            case '`':
              return "%W";
            default:
              if (character <= 26) {
                return "$" + (char) ('A' + (character - 1));
              } else if (character < ' ') {
                return "%" + (char) ('A' + (character - 27));
              } else if (character <= ',' || character == '/' || character == ':') {
                return "/" + (char) ('A' + (character - 33));
              } else if (character <= '9') {
                return String.valueOf((char) ('0' + (character - 48)));
              } else if (character <= '?') {
                return "%" + (char) ('F' + (character - 59));
              } else if (character <= 'Z') {
                return String.valueOf((char) ('A' + (character - 65)));
              } else if (character <= '_') {
                return "%" + (char) ('K' + (character - 91));
              } else if (character <= 'z') {
                return "+" + (char) ('A' + (character - 97));
              } else if (character <= 127) {
                return "%" + (char) ('P' + (character - 123));
              } else {
                throw new IllegalArgumentException(
                    "Requested content contains a non-encodable character: '" + contents.charAt(i) + "'");
              }
          }
      }
}
