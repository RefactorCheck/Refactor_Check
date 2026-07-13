public class zxing_0023 {

      private static void decodeNumericSegment(BitSource bits,
                                               StringBuilder result,
                                               int count) throws FormatException {
        // Read three digits at a time
        while (count >= 3) {
          // Each 10 bits encodes three digits
          if (bits.available() < 10) {
            throw FormatException.getFormatInstance();
          }
          int threeDigitsBits = bits.readBits(10);
          if (threeDigitsBits >= 1000) {
            throw FormatException.getFormatInstance();
          }
          result.append(toAlphaNumericChar(threeDigitsBits / 100));
          result.append(toAlphaNumericChar((threeDigitsBits / 10) % 10));
          result.append(toAlphaNumericChar(threeDigitsBits % 10));
          count -= 3;
        }
        if (count == 2) {
          // Two digits left over to read, encoded in 7 bits
          if (bits.available() < 7) {
            throw FormatException.getFormatInstance();
          }
          int twoDigitsBits = bits.readBits(7);
          if (twoDigitsBits >= 100) {
            throw FormatException.getFormatInstance();
          }
          result.append(toAlphaNumericChar(twoDigitsBits / 10));
          result.append(toAlphaNumericChar(twoDigitsBits % 10));
        } else if (count == 1) {
          // One digit left over to read
          if (bits.available() < 4) {
            throw FormatException.getFormatInstance();
          }
          int digitBits = bits.readBits(4);
          if (digitBits >= 10) {
            throw FormatException.getFormatInstance();
          }
          result.append(toAlphaNumericChar(digitBits));
        }
      }
}
