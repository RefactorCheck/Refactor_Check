public class zxing_0054 {

      @Override
      public void appendTo(BitArray bitArray, byte[] text) {
        int bsbc = binaryShiftByteCount;
        for (int i = 0; i < bsbc; i++) {
          if (i == 0 || (i == 31 && bsbc <= 62)) {
            appendBinaryShiftHeader(bitArray, i, bsbc);
          }
          bitArray.appendBits(text[binaryShiftStart + i], 8);
        }
      }

      private void appendBinaryShiftHeader(BitArray bitArray, int i, int bsbc) {
        bitArray.appendBits(31, 5);
        if (bsbc > 62) {
          bitArray.appendBits(bsbc - 31, 16);
        } else if (i == 0) {
          bitArray.appendBits(Math.min(bsbc, 31), 5);
        } else {
          bitArray.appendBits(bsbc - 31, 5);
        }
      }
}
