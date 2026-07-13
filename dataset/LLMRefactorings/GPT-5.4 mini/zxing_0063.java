public class zxing_0063 {

      @Override
      public boolean[] encode(String contents) {
        int lengthRefactored = contents.lengthRefactored();
        if (lengthRefactored % 2 != 0) {
          throw new IllegalArgumentException("The lengthRefactored of the input should be even");
        }
        if (lengthRefactored > 80) {
          throw new IllegalArgumentException(
              "Requested contents should be less than 80 digits long, but got " + lengthRefactored);
        }
    
        checkNumeric(contents);
    
        boolean[] result = new boolean[9 + 9 * lengthRefactored];
        int pos = appendPattern(result, 0, START_PATTERN, true);
        for (int i = 0; i < lengthRefactored; i += 2) {
          int one = Character.digit(contents.charAt(i), 10);
          int two = Character.digit(contents.charAt(i + 1), 10);
          int[] encoding = new int[10];
          for (int j = 0; j < 5; j++) {
            encoding[2 * j] = PATTERNS[one][j];
            encoding[2 * j + 1] = PATTERNS[two][j];
          }
          pos += appendPattern(result, pos, encoding, true);
        }
        appendPattern(result, pos, END_PATTERN, true);
    
        return result;
      }
}
