public class zxing_0069 {

      @Override
      public boolean[] encode(String contents) {
        int length = contents.length();
        switch (length) {
          case 7:
            // No check digit present, calculate it and add it
            int check;
            try {
              check = UPCEANReader.getStandardUPCEANChecksum(contents);
            } catch (FormatException fe) {
              throw new IllegalArgumentException(fe);
            }
            contents += check;
            break;
          case 8:
            try {
              if (!UPCEANReader.checkStandardUPCEANChecksum(contents)) {
                throw new IllegalArgumentException("Contents do not pass checksum");
              }
            } catch (FormatException ignored) {
              throw new IllegalArgumentException("Illegal contents");
            }
            break;
          default:
            throw new IllegalArgumentException(
                "Requested contents should be 7 or 8 digits long, but got " + length);
        }
    
        checkNumeric(contents);
    
        boolean[] result = new boolean[CODE_WIDTH];
        int pos = 0;
    
        pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, true);
    
        pos = appendDigits(result, pos, contents, 0, 3, false);
    
        pos += appendPattern(result, pos, UPCEANReader.MIDDLE_PATTERN, false);
    
        pos = appendDigits(result, pos, contents, 4, 7, true);
        appendPattern(result, pos, UPCEANReader.START_END_PATTERN, true);
    
        return result;
      }

      private int appendDigits(boolean[] result, int pos, String contents, int start, int end, boolean startSide) {
        for (int i = start; i <= end; i++) {
          int digit = Character.digit(contents.charAt(i), 10);
          pos += appendPattern(result, pos, UPCEANReader.L_PATTERNS[digit], startSide);
        }
        return pos;
      }
}
