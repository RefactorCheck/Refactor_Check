public class zxing_0206 {

      private static final int PADDING_PATTERN_EVEN = 0xEC;
      private static final int PADDING_PATTERN_ODD = 0x11;

      static void terminateBits(int numDataBytes, BitArray bits) throws WriterException {
        int capacity = numDataBytes * 8;
        if (bits.getSize() > capacity) {
          throw new WriterException("data bits cannot fit in the QR Code" + bits.getSize() + " > " +
              capacity);
        }
        // Append Mode.TERMINATE if there is enough space (value is 0000)
        for (int i = 0; i < 4 && bits.getSize() < capacity; ++i) {
          bits.appendBit(false);
        }
        // Append termination bits. See 8.4.8 of JISX0510:2004 (p.24) for details.
        // If the last byte isn't 8-bit aligned, we'll add padding bits.
        int numBitsInLastByte = bits.getSize() & 0x07;
        if (numBitsInLastByte > 0) {
          for (int i = numBitsInLastByte; i < 8; i++) {
            bits.appendBit(false);
          }
        }
        // If we have more space, we'll fill the space with padding patterns defined in 8.4.9 (p.24).
        int numPaddingBytes = numDataBytes - bits.getSizeInBytes();
        for (int i = 0; i < numPaddingBytes; ++i) {
          bits.appendBits((i & 0x01) == 0 ? PADDING_PATTERN_EVEN : PADDING_PATTERN_ODD, 8);
        }
        if (bits.getSize() != capacity) {
          throw new WriterException("Bits size does not equal capacity");
        }
      }
}
