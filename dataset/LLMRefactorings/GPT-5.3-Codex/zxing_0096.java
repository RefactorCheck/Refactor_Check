public class zxing_0096 {

      private static void decodeAnsiX12SegmentRefactored(BitSource bits,
                                               ECIStringBuilder result) throws FormatException {
        // Three ANSI X12 values are encoded in a 16-bit value as
        // (1600 * C1) + (40 * C2) + C3 + 1
    
        int[] cValues = new int[3];
        do {
          // If there is only one byte left then it will be encoded as ASCII
          if (bits.available() == 8) {
            return;
          }
          int firstByte = bits.readBits(8);
          if (firstByte == 254) {  // Unlatch codeword
            return;
          }
    
          parseTwoBytes(firstByte, bits.readBits(8), cValues);
    
          for (int i = 0; i < 3; i++) {
            int cValue = cValues[i];
            switch (cValue) {
              case 0: // X12 segment terminator <CR>
                result.append('\r');
                break;
              case 1: // X12 segment separator *
                result.append('*');
                break;
              case 2: // X12 sub-element separator >
                result.append('>');
                break;
              case 3: // space
                result.append(' ');
                break;
              default:
                if (cValue < 14) {  // 0 - 9
                  result.append((char) (cValue + 44));
                } else if (cValue < 40) {  // A - Z
                  result.append((char) (cValue + 51));
                } else {
                  throw FormatException.getFormatInstance();
                }
                break;
            }
          }
        } while (bits.available() > 0);
      }
}
