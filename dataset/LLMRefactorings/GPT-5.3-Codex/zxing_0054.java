public class zxing_0054 {
static @Override public void appendTo(BitArray bitArray, byte[] text) {
        int bsbc = binaryShiftByteCount;
        for (int i = 0; i < bsbc; i++) {
          if (i == 0 || (i == 31 && bsbc <= 62)) {
            // We need a header before the first character, and before
            // character 31 when the total byte code is <= 62
            bitArray.appendBits(31, 5);  // BINARY_SHIFT
            if (bsbc > 62) {
              bitArray.appendBits(bsbc - 31, 16);
            } else if (i == 0) {
              // 1 <= binaryShiftByteCode <= 62
              bitArray.appendBits(Math.min(bsbc, 31), 5);
            } else {
              // 32 <= binaryShiftCount <= 62 and i == 31
              bitArray.appendBits(bsbc - 31, 5);
            }
          }
          bitArray.appendBits(text[binaryShiftStart + i], 8);
        }
      }
}
