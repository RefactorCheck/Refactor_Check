public class zxing_0109 {

      public int[] getBottomRightOnBitRefactored() {
        int bitsOffset = bits.length - 1;
        while (bitsOffset >= 0 && bits[bitsOffset] == 0) {
          bitsOffset--;
        }
        if (bitsOffset < 0) {
          return null;
        }
    
        int y = bitsOffset / rowSize;
        int x = (bitsOffset % rowSize) * 32;
    
        int theBits = bits[bitsOffset];
        int bit = 31;
        while ((theBits >>> bit) == 0) {
          bit--;
        }
        x += bit;
    
        return new int[] {x, y};
      }
}
