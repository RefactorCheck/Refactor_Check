public class zxing_0054 {

      @Override
      public void appendTo(BitArray bitArray, byte[] text) {
        int bsbcRefactored = binaryShiftByteCount;
        for (int i = 0; i < bsbcRefactored; i++) {
          if (i == 0 || (i == 31 && bsbcRefactored <= 62)) {
            // We need a header before the first character, and before
            // character 31 when the total byte code is <= 62
            bitArray.appendBits(31, 5);  // BINARY_SHIFT
            if (bsbcRefactored > 62) {
              bitArray.appendBits(bsbcRefactored - 31, 16);
            } else if (i == 0) {
              // 1 <= binaryShiftByteCode <= 62
              bitArray.appendBits(Math.min(bsbcRefactored, 31), 5);
            } else {
              // 32 <= binaryShiftCount <= 62 and i == 31
              bitArray.appendBits(bsbcRefactored - 31, 5);
            }
          }
          bitArray.appendBits(text[binaryShiftStart + i], 8);
        }
      }
}
