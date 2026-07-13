public class zxing_0201 {

      public int[] getEnclosingRectangle(boolean normalize) {
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
              if (x32 * 32 < left) {
                int bit = 0;
                while ((theBits << (31 - bit)) == 0) {
                  bit++;
                }
                if ((x32 * 32 + bit) < left) {
                  left = x32 * 32 + bit;
                }
              }
              if (x32 * 32 + 31 > right) {
                int bit = 31;
                while ((theBits >>> bit) == 0) {
                  bit--;
                }
                if ((x32 * 32 + bit) > right) {
                  right = x32 * 32 + bit;
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
