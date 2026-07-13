public class zxing_0220 {

      private static int sampleGridColumn(int x, int y, int left, int width) {
        return left + Math.min(
            (x * width + width / 2 + (y & 0x01) * width / 2) / MATRIX_WIDTH,
            width - 1);
      }

      private static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {
    
        int[] enclosingRectangle = image.getEnclosingRectangle();
        if (enclosingRectangle == null) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        int left = enclosingRectangle[0];
        int top = enclosingRectangle[1];
        int width = enclosingRectangle[2];
        int height = enclosingRectangle[3];
    
        BitMatrix bits = new BitMatrix(MATRIX_WIDTH, MATRIX_HEIGHT);
        for (int y = 0; y < MATRIX_HEIGHT; y++) {
          int iy = top + Math.min((y * height + height / 2) / MATRIX_HEIGHT, height - 1);
          for (int x = 0; x < MATRIX_WIDTH; x++) {
            int ix = sampleGridColumn(x, y, left, width);
            if (image.get(ix, iy)) {
              bits.set(x, y);
            }
          }
        }
        return bits;
      }
}
