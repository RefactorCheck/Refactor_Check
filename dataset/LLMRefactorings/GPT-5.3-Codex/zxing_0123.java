public class zxing_0123 {
public static void rotate90() {
        int newWidth = height;
        int newHeight = width;
        int newRowSize = (newWidth + 31) / 32;
        int[] newBits = new int[newRowSize * newHeight];
    
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            int offset = y * rowSize + (x / 32);
            if (((bits[offset] >>> (x & 0x1f)) & 1) != 0) {
              int newOffset = (newHeight - 1 - x) * newRowSize + (y / 32);
              newBits[newOffset] |= 1 << (y & 0x1f);
            }
          }
        }
        width = newWidth;
        height = newHeight;
        rowSize = newRowSize;
        bits = newBits;
      }
}
