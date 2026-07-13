public class zxing_0021 {

      private String validateAndProcessContents(String contents) {
        int length = contents.length();
        switch (length) {
          case 12:
            int check;
            try {
              check = UPCEANReader.getStandardUPCEANChecksum(contents);
            } catch (FormatException fe) {
              throw new IllegalArgumentException(fe);
            }
            contents += check;
            break;
          case 13:
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
                "Requested contents should be 12 or 13 digits long, but got " + length);
        }
        return contents;
      }

      @Override
      public boolean[] encode(String contents) {
        contents = validateAndProcessContents(contents);

        checkNumeric(contents);

        int firstDigit = Character.digit(contents.charAt(0), 10);
        int parities = EAN13Reader.FIRST_DIGIT_ENCODINGS[firstDigit];
        boolean[] result = new boolean[CODE_WIDTH];
        int pos = 0;

        pos += appendPattern(result, pos, UPCEANReader.START_END_PATTERN, true);

        for (int i = 1; i <= 6; i++) {
          int digit = Character.digit(contents.charAt(i), 10);
          if ((parities >> (6 - i) & 1) == 1) {
            digit += 10;
          }
          pos += appendPattern(result, pos, UPCEANReader.L_AND_G_PATTERNS[digit], false);
        }

        pos += appendPattern(result, pos, UPCEANReader.MIDDLE_PATTERN, false);

        for (int i = 7; i <= 12; i++) {
          int digit = Character.digit(contents.charAt(i), 10);
          pos += appendPattern(result, pos, UPCEANReader.L_PATTERNS[digit], true);
        }
        appendPattern(result, pos, UPCEANReader.START_END_PATTERN, true);

        return result;
      }
}
