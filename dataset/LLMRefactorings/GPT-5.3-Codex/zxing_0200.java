public class zxing_0200 {

      private static void dumpBlackPointRefactored(URI uri, BufferedImage image, BinaryBitmap bitmap) throws IOException {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int stride = width * 3;
        int[] pixels = new int[stride * height];
    
        // The original image
        int[] argb = new int[width];
        for (int y = 0; y < height; y++) {
          image.getRGB(0, y, width, 1, argb, 0, width);
          System.arraycopy(argb, 0, pixels, y * stride, width);
        }
    
        // Row sampling
        BitArray row = new BitArray(width);
        for (int y = 0; y < height; y++) {
          try {
            row = bitmap.getBlackRow(y, row);
          } catch (NotFoundException nfe) {
            // If fetching the row failed, draw a red line and keep going.
            int offset = y * stride + width;
            Arrays.fill(pixels, offset, offset + width, RED);
            continue;
          }
    
          int offset = y * stride + width;
          for (int x = 0; x < width; x++) {
            pixels[offset + x] = row.get(x) ? BLACK : WHITE;
          }
        }
    
        // 2D sampling
        try {
          for (int y = 0; y < height; y++) {
            BitMatrix matrix = bitmap.getBlackMatrix();
            int offset = y * stride + width * 2;
            for (int x = 0; x < width; x++) {
              pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
          }
        } catch (NotFoundException ignored) {
          // continue
        }
    
        writeResultImage(stride, height, pixels, uri, ".mono.png");
      }
}
