public class zxing_0130 {

      @Override
      public byte[] getMatrix() {
        int width = getWidth();
        int height = getHeight();

        if (width == dataWidth && height == dataHeight) {
          return luminances;
        }

        int area = width * height;
        byte[] matrix = new byte[area];
        int inputOffset = top * dataWidth + left;

        if (width == dataWidth) {
          System.arraycopy(luminances, inputOffset, matrix, 0, area);
          return matrix;
        }

        copyCroppedRows(matrix, width, height, inputOffset);
        return matrix;
      }

      private void copyCroppedRows(byte[] matrix, int width, int height, int inputOffset) {
        for (int y = 0; y < height; y++) {
          int outputOffset = y * width;
          System.arraycopy(luminances, inputOffset, matrix, outputOffset, width);
          inputOffset += dataWidth;
        }
      }
}
