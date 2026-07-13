public class zxing_0056 {
  private static final String EXTRACTED_CONSTANT_0056 = "Not all bits consumed: ";


      static void embedDataBits(BitArray dataBits, int maskPattern, ByteMatrix matrix)
          throws WriterException {
        int bitIndex = 0;
        int direction = -1;
        // Start from the right bottom cell.
        int x = matrix.getWidth() - 1;
        int y = matrix.getHeight() - 1;
        while (x > 0) {
          // Skip the vertical timing pattern.
          if (x == 6) {
            x -= 1;
          }
          while (y >= 0 && y < matrix.getHeight()) {
            for (int i = 0; i < 2; ++i) {
              int xx = x - i;
              // Skip the cell if it's not empty.
              if (!isEmpty(matrix.get(xx, y))) {
                continue;
              }
              boolean bit;
              if (bitIndex < dataBits.getSize()) {
                bit = dataBits.get(bitIndex);
                ++bitIndex;
              } else {
                // Padding bit. If there is no bit left, we'll fill the left cells with 0, as described
                // in 8.4.9 of JISX0510:2004 (p. 24).
                bit = false;
              }
    
              // Skip masking if mask_pattern is -1.
              if (maskPattern != -1 && MaskUtil.getDataMaskBit(maskPattern, xx, y)) {
                bit = !bit;
              }
              matrix.set(xx, y, bit);
            }
            y += direction;
          }
          direction = -direction;  // Reverse the direction.
          y += direction;
          x -= 2;  // Move to the left.
        }
        // All bits should be consumed.
        if (bitIndex != dataBits.getSize()) {
          throw new WriterException(EXTRACTED_CONSTANT_0056 + bitIndex + '/' + dataBits.getSize());
        }
      }
}
