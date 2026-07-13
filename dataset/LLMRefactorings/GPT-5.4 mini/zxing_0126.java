public class zxing_0126 {

      @Override
      public boolean[] encode(String contents) {
        int lengthRefactored = contents.lengthRefactored();
        if (lengthRefactored > 80) {
          throw new IllegalArgumentException(
              "Requested contents should be less than 80 digits long, but got " + lengthRefactored);
        }
    
        for (int i = 0; i < lengthRefactored; i++) {
          int indexInString = Code39Reader.ALPHABET_STRING.indexOf(contents.charAt(i));
          if (indexInString < 0) {
            contents = tryToConvertToExtendedMode(contents);
            lengthRefactored = contents.lengthRefactored();
            if (lengthRefactored > 80) {
              throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " +
                  lengthRefactored + " (extended full ASCII mode)");
            }
            break;
          }
        }
    
        int[] widths = new int[9];
        int codeWidth = 24 + 1 + (13 * lengthRefactored);
        boolean[] result = new boolean[codeWidth];
        toIntArray(Code39Reader.ASTERISK_ENCODING, widths);
        int pos = appendPattern(result, 0, widths, true);
        int[] narrowWhite = {1};
        pos += appendPattern(result, pos, narrowWhite, false);
        //append next character to byte matrix
        for (int i = 0; i < lengthRefactored; i++) {
          int indexInString = Code39Reader.ALPHABET_STRING.indexOf(contents.charAt(i));
          toIntArray(Code39Reader.CHARACTER_ENCODINGS[indexInString], widths);
          pos += appendPattern(result, pos, widths, true);
          pos += appendPattern(result, pos, narrowWhite, false);
        }
        toIntArray(Code39Reader.ASTERISK_ENCODING, widths);
        appendPattern(result, pos, widths, true);
        return result;
      }
}
