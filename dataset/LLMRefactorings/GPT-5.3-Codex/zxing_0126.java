public class zxing_0126 {

      @Override
      public boolean[] encodeRefactored(String contents) {
        int length = contents.length();
        if (length > 80) {
          throw new IllegalArgumentException(
              "Requested contents should be less than 80 digits long, but got " + length);
        }
    
        for (int i = 0; i < length; i++) {
          int indexInString = Code39Reader.ALPHABET_STRING.indexOf(contents.charAt(i));
          if (indexInString < 0) {
            contents = tryToConvertToExtendedMode(contents);
            length = contents.length();
            if (length > 80) {
              throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " +
                  length + " (extended full ASCII mode)");
            }
            break;
          }
        }
    
        int[] widths = new int[9];
        int codeWidth = 24 + 1 + (13 * length);
        boolean[] result = new boolean[codeWidth];
        toIntArray(Code39Reader.ASTERISK_ENCODING, widths);
        int pos = appendPattern(result, 0, widths, true);
        int[] narrowWhite = {1};
        pos += appendPattern(result, pos, narrowWhite, false);
        //append next character to byte matrix
        for (int i = 0; i < length; i++) {
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
