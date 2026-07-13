public class zxing_0109 {

      public int[] getBottomRightOnBit() {
        int bitsOffset = lastNonZeroBitOffset();
        if (bitsOffset < 0) {
          return null;
        }
    
        int y = bitsOffset / rowSize;
        int x = (bitsOffset % rowSize) * 32;
        x += highestSetBit(bits[bitsOffset]);
    
        return new int[] {x, y};
      }

      private int lastNonZeroBitOffset() {
        int bitsOffset = bits.length - 1;
        while (bitsOffset >= 0 && bits[bitsOffset] == 0) {
          bitsOffset--;
        }
        return bitsOffset;
      }

      private int highestSetBit(int theBits) {
        int bit = 31;
        while ((theBits >>> bit) == 0) {
          bit--;
        }
        return bit;
      }
}
