public class zxing_0049 {

      private static final int RESULT_INITIAL_CAPACITY = 144;
      private static final int MODE_2_POSTCODE_MAX_LENGTH = 10;
      private static final int MESSAGE_START_POSITION = 10;
      private static final int MESSAGE_END_POSITION = 84;
      private static final int HEADER_INSERT_POSITION = 9;
      private static final int MODE_4_MESSAGE_LENGTH = 93;
      private static final int MODE_5_MESSAGE_LENGTH = 77;
      private static final String THREE_DIGIT_PATTERN = "000";
      private static final String TEN_DIGIT_PATTERN = "0000000000";
      private static final String HEADER_PREFIX = "[)>" + RS + "01" + GS;

      static DecoderResult decode(byte[] bytes, int mode) throws FormatException {
        StringBuilder result = new StringBuilder(RESULT_INITIAL_CAPACITY);
        switch (mode) {
          case 2:
          case 3:
            String postcode;
            if (mode == 2) {
              int pc = getPostCode2(bytes);
              int ps2Length = getPostCode2Length(bytes);
              if (ps2Length > MODE_2_POSTCODE_MAX_LENGTH) {
                throw FormatException.getFormatInstance();
              }
              NumberFormat df = new DecimalFormat(TEN_DIGIT_PATTERN.substring(0, ps2Length),
                  DecimalFormatSymbols.getInstance(Locale.ROOT));
              postcode = df.format(pc);
            } else {
              postcode = getPostCode3(bytes);
            }
            NumberFormat threeDigits = new DecimalFormat(THREE_DIGIT_PATTERN, DecimalFormatSymbols.getInstance(Locale.ROOT));
            String country = threeDigits.format(getCountry(bytes));
            String service = threeDigits.format(getServiceClass(bytes));
            result.append(getMessage(bytes, MESSAGE_START_POSITION, MESSAGE_END_POSITION));
            if (result.toString().startsWith(HEADER_PREFIX)) {
              if (result.length() < HEADER_INSERT_POSITION) {
                throw FormatException.getFormatInstance();
              }
              result.insert(HEADER_INSERT_POSITION, postcode + GS + country + GS + service + GS);
            } else {
              result.insert(0, postcode + GS + country + GS + service + GS);
            }
            break;
          case 4:
            result.append(getMessage(bytes, 1, MODE_4_MESSAGE_LENGTH));
            break;
          case 5:
            result.append(getMessage(bytes, 1, MODE_5_MESSAGE_LENGTH));
            break;
        }
        return new DecoderResult(bytes, result.toString(), null, String.valueOf(mode));
      }
}
