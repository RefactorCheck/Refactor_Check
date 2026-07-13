public class zxing_0149 {

      State addBinaryShiftChar(int index) {
        Token token = this.token;
        int mode = this.mode;
        int bitCount = this.bitCount;
        if (this.mode == HighLevelEncoder.MODE_PUNCT || this.mode == HighLevelEncoder.MODE_DIGIT) {
          int latch = HighLevelEncoder.LATCH_TABLE[mode][HighLevelEncoder.MODE_UPPER];
          token = token.add(latch & 0xFFFF, latch >> 16);
          bitCount += latch >> 16;
          mode = HighLevelEncoder.MODE_UPPER;
        }
        int deltaBitCount = calculateDeltaBitCount();
        State result = new State(token, mode, binaryShiftByteCount + 1, bitCount + deltaBitCount);
        if (result.binaryShiftByteCount == 2047 + 31) {
          // The string is as long as it's allowed to be.  We should end it.
          result = result.endBinaryShift(index + 1);
        }
        return result;
      }
      
      private int calculateDeltaBitCount() {
        return (binaryShiftByteCount == 0 || binaryShiftByteCount == 31) ? 18 :
               (binaryShiftByteCount == 62) ? 9 : 8;
      }
}
