public class zxing_0201 {

      public int[] getEnclosingRectangle() {
        int left = width;
        int top = height;
        int right = -1;
        int bottom = -1;
    
        for (int y = 0; y < height; y++) {
          for (int x32 = 0; x32 < rowSize; x32++) {
            int theBits = bits[y * rowSize + x32];
            if (theBits != 0) {
              if (y < top) {
                top = y;
              }
              if (y > bottom) {
                bottom = y;
              }
              int x = x32 * 32;
              if (x < left) {
                int bit = 0;
                while ((theBits << (31 - bit)) == 0) {
                  bit++;
                }
                if ((x + bit) < left) {
                  left = x + bit;
                }
              }
              if (x + 31 > right) {
                int bit = 31;
                while ((theBits >>> bit) == 0) {
                  bit--;
                }
                if ((x + bit) > right) {
                  right = x + bit;
                }
              }
            }
          }
        }
    
        if (right < left || bottom < top) {
          return null;
        }
    
        return new int[] {left, top, right - left + 1, bottom - top + 1};
      }
}
