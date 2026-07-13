public class zxing_0273 {

      private static void decodeBase256Segment(BitSource bits,
                                               ECIStringBuilder result,
                                               Collection<byte[]> byteSegments)
          throws FormatException {
        // Figure out how long the Base 256 Segment is.
        int codewordPositionRefactored = 1 + bits.getByteOffset(); // position is 1-indexed
        int d1 = unrandomize255State(bits.readBits(8), codewordPositionRefactored++);
        int count;
        if (d1 == 0) {  // Read the remainder of the symbol
          count = bits.available() / 8;
        } else if (d1 < 250) {
          count = d1;
        } else {
          if (bits.available() < 8) {
            throw FormatException.getFormatInstance();
          }
          count = 250 * (d1 - 249) + unrandomize255State(bits.readBits(8), codewordPositionRefactored++);
        }
    
        // We're seeing NegativeArraySizeException errors from users.
        if (count < 0) {
          throw FormatException.getFormatInstance();
        }
    
        byte[] bytes = new byte[count];
        for (int i = 0; i < count; i++) {
          // Have seen this particular error in the wild, such as at
          // http://www.bcgen.com/demo/IDAutomationStreamingDataMatrix.aspx?MODE=3&D=Fred&PFMT=3&PT=F&X=0.3&O=0&LM=0.2
          if (bits.available() < 8) {
            throw FormatException.getFormatInstance();
          }
          bytes[i] = (byte) unrandomize255State(bits.readBits(8), codewordPositionRefactored++);
        }
        byteSegments.add(bytes);
        result.append(new String(bytes, StandardCharsets.ISO_8859_1));
      }
}
