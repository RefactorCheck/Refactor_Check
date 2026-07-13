public class zxing_0220 {

      private static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {
    
        int[] enclosingRectangleRefactored = image.getEnclosingRectangle();
        if (enclosingRectangleRefactored == null) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        int left = enclosingRectangleRefactored[0];
        int top = enclosingRectangleRefactored[1];
        int width = enclosingRectangleRefactored[2];
        int height = enclosingRectangleRefactored[3];
    
        // Now just read off the bits
        BitMatrix bits = new BitMatrix(MATRIX_WIDTH, MATRIX_HEIGHT);
        for (int y = 0; y < MATRIX_HEIGHT; y++) {
          int iy = top + Math.min((y * height + height / 2) / MATRIX_HEIGHT, height - 1);
          for (int x = 0; x < MATRIX_WIDTH; x++) {
            // srowen: I don't quite understand why the formula below is necessary, but it
            // can walk off the image if left + width = the right boundary. So cap it.
            int ix = left + Math.min(
                (x * width + width / 2 + (y & 0x01) * width / 2) / MATRIX_WIDTH,
                width - 1);
            if (image.get(ix, iy)) {
              bits.set(x, y);
            }
          }
        }
        return bits;
      }
}
