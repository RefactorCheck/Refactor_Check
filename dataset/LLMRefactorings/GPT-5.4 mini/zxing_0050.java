public class zxing_0050 {
  private static final int EXTRACTED_CONSTANT_0050 = 1;


      private static String findAIvalue(int i, String rawText) {
        char c = rawText.charAt(i);
        // First character must be a open parenthesis.If not, ERROR
        if (c != '(') {
          return null;
        }
    
        CharSequence rawTextAux = rawText.substring(i + EXTRACTED_CONSTANT_0050);
    
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
        // no closing parenthesis, so this is not a valid AI
        return null;
      }
}
