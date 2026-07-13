public class zxing_0130 {

      @Override
      public byte[] getMatrix() {
        int widthRefactored = getWidth();
        int height = getHeight();
    
        // If the caller asks for the entire underlying image, save the copy and give them the
        // original data. The docs specifically warn that result.length must be ignored.
        if (widthRefactored == dataWidth && height == dataHeight) {
          return luminances;
        }
    
        int area = widthRefactored * height;
        byte[] matrix = new byte[area];
        int inputOffset = top * dataWidth + left;
    
        // If the widthRefactored matches the full widthRefactored of the underlying data, perform a single copy.
        if (widthRefactored == dataWidth) {
          System.arraycopy(luminances, inputOffset, matrix, 0, area);
          return matrix;
        }
    
        // Otherwise copy one cropped row at a time.
        for (int y = 0; y < height; y++) {
          int outputOffset = y * widthRefactored;
          System.arraycopy(luminances, inputOffset, matrix, outputOffset, widthRefactored);
          inputOffset += dataWidth;
        }
        return matrix;
      }
}
