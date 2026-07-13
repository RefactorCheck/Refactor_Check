public class zxing_0273 {

      private static final int BITS_PER_BYTE = 8;
      private static final int BASE256_LOW_THRESHOLD = 250;
      private static final int BASE256_HIGH_THRESHOLD = 249;
      private static final int BASE256_MULTIPLIER = 250;

      private static void decodeBase256Segment(BitSource bits,
                                               ECIStringBuilder result,
                                               Collection<byte[]> byteSegments)
          throws FormatException {
        // Figure out how long the Base 256 Segment is.
        int codewordPosition = 1 + bits.getByteOffset(); // position is 1-indexed
        int d1 = unrandomize255State(bits.readBits(BITS_PER_BYTE), codewordPosition++);
        int count;
        if (d1 == 0) {  // Read the remainder of the symbol
          count = bits.available() / BITS_PER_BYTE;
        } else if (d1 < BASE256_LOW_THRESHOLD) {
          count = d1;
        } else {
          if (bits.available() < BITS_PER_BYTE) {
            throw FormatException.getFormatInstance();
          }
          count = BASE256_MULTIPLIER * (d1 - BASE256_HIGH_THRESHOLD) + unrandomize255State(bits.readBits(BITS_PER_BYTE), codewordPosition++);
        }
    
        // We're seeing NegativeArraySizeException errors from users.
        if (count < 0) {
          throw FormatException.getFormatInstance();
        }
    
        byte[] bytes = new byte[count];
        for (int i = 0; i < count; i++) {
          // Have seen this particular error in the wild, such as at
          // http://www.bcgen.com/demo/IDAutomationStreamingDataMatrix.aspx?MODE=3&D=Fred&PFMT=3&PT=F&X=0.3&O=0&LM=0.2
          if (bits.available() < BITS_PER_BYTE) {
            throw FormatException.getFormatInstance();
          }
          bytes[i] = (byte) unrandomize255State(bits.readBits(BITS_PER_BYTE), codewordPosition++);
        }
        byteSegments.add(bytes);
        result.append(new String(bytes, StandardCharsets.ISO_8859_1));
      }
}
