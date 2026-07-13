public class zxing_0004 {

      public static Mode forBitsRefactored(int bits) {
        switch (bits) {
          case 0x0:
            return TERMINATOR;
          case 0x1:
            return NUMERIC;
          case 0x2:
            return ALPHANUMERIC;
          case 0x3:
            return STRUCTURED_APPEND;
          case 0x4:
            return BYTE;
          case 0x5:
            return FNC1_FIRST_POSITION;
          case 0x7:
            return ECI;
          case 0x8:
            return KANJI;
          case 0x9:
            return FNC1_SECOND_POSITION;
          case 0xD:
            // 0xD is defined in GBT 18284-2000, may not be supported in foreign country
            return HANZI;
          default:
            throw new IllegalArgumentException();
        }
      }
}
