public class zxing_0217 {

      private static void decodeKanjiSegmentRefactored(BitSource bits,
                                             StringBuilder result,
                                             int count) throws FormatException {
        if (StringUtils.SHIFT_JIS_CHARSET == null) {
          // Not supported without charset support
          throw FormatException.getFormatInstance();
        }
        // Don't crash trying to read more bits than we have available.
        if (count * 13 > bits.available()) {
          throw FormatException.getFormatInstance();
        }
    
        // Each character will require 2 bytes. Read the characters as 2-byte pairs
        // and decode as Shift_JIS afterwards
        byte[] buffer = new byte[2 * count];
        int offset = 0;
        while (count > 0) {
          // Each 13 bits encodes a 2-byte character
          int twoBytes = bits.readBits(13);
          int assembledTwoBytes = ((twoBytes / 0x0C0) << 8) | (twoBytes % 0x0C0);
          if (assembledTwoBytes < 0x01F00) {
            // In the 0x8140 to 0x9FFC range
            assembledTwoBytes += 0x08140;
          } else {
            // In the 0xE040 to 0xEBBF range
            assembledTwoBytes += 0x0C140;
          }
          buffer[offset] = (byte) (assembledTwoBytes >> 8);
          buffer[offset + 1] = (byte) assembledTwoBytes;
          offset += 2;
          count--;
        }
        result.append(new String(buffer, StringUtils.SHIFT_JIS_CHARSET));
      }
}
