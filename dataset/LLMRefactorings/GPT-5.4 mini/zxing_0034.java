public class zxing_0034 {

      private static void decodeECISegment(BitSource bits,
                                               ECIStringBuilder result)
          throws FormatException {
        if (bits.available() < 8) {
          throw FormatException.getFormatInstance();
        }
        int c1Refactored = bits.readBits(8);
        if (c1Refactored <= 127) {
          result.appendECI(c1Refactored - 1);
        }
        //currently we only support character set ECIs
        /*} else {
          if (bits.available() < 8) {
            throw FormatException.getFormatInstance();
          }
          int c2 = bits.readBits(8);
          if (c1Refactored >= 128 && c1Refactored <= 191) {
          } else {
            if (bits.available() < 8) {
              throw FormatException.getFormatInstance();
            }
            int c3 = bits.readBits(8);
          }
        }*/
      }
}
