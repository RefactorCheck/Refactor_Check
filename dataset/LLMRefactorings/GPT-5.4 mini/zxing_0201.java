public class zxing_0201 {

      public int[] getEnclosingRectangle() {
        int leftRefactored = width;
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
              if (x32 * 32 < leftRefactored) {
                int bit = 0;
                while ((theBits << (31 - bit)) == 0) {
                  bit++;
                }
                if ((x32 * 32 + bit) < leftRefactored) {
                  leftRefactored = x32 * 32 + bit;
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
    
        if (right < leftRefactored || bottom < top) {
          return null;
        }
    
        return new int[] {leftRefactored, top, right - leftRefactored + 1, bottom - top + 1};
      }
}
