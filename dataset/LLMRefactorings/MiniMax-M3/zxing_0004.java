public class zxing_0004 {

      private static final int BITS_TERMINATOR = 0x0;
      private static final int BITS_NUMERIC = 0x1;
      private static final int BITS_ALPHANUMERIC = 0x2;
      private static final int BITS_STRUCTURED_APPEND = 0x3;
      private static final int BITS_BYTE = 0x4;
      private static final int BITS_FNC1_FIRST_POSITION = 0x5;
      private static final int BITS_ECI = 0x7;
      private static final int BITS_KANJI = 0x8;
      private static final int BITS_FNC1_SECOND_POSITION = 0x9;
      private static final int BITS_HANZI = 0xD;

      public static Mode forBits(int bits) {
        switch (bits) {
          case BITS_TERMINATOR:
            return TERMINATOR;
          case BITS_NUMERIC:
            return NUMERIC;
          case BITS_ALPHANUMERIC:
            return ALPHANUMERIC;
          case BITS_STRUCTURED_APPEND:
            return STRUCTURED_APPEND;
          case BITS_BYTE:
            return BYTE;
          case BITS_FNC1_FIRST_POSITION:
            return FNC1_FIRST_POSITION;
          case BITS_ECI:
            return ECI;
          case BITS_KANJI:
            return KANJI;
          case BITS_FNC1_SECOND_POSITION:
            return FNC1_SECOND_POSITION;
          case BITS_HANZI:
            // 0xD is defined in GBT 18284-2000, may not be supported in foreign country
            return HANZI;
          default:
            throw new IllegalArgumentException();
        }
      }
}
