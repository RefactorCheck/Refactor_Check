public class zxing_0034 {

      private static void decodeECISegment(BitSource bits,
                                               ECIStringBuilder result)
          throws FormatException {
        if (bits.available() < 8) {
          throw FormatException.getFormatInstance();
        }
        int c1 = bits.readBits(8);
        if (c1 <= 127) {
          result.appendECI(c1 - 1);
        }
        //currently we only support character set ECIs
        /*} else {
          if (bits.available() < 8) {
            throw FormatException.getFormatInstance();
          }
          int c2 = bits.readBits(8);
          if (c1 >= 128 && c1 <= 191) {
          } else {
            if (bits.available() < 8) {
              throw FormatException.getFormatInstance();
            }
            int c3 = bits.readBits(8);
          }
        }*/
      }
}
