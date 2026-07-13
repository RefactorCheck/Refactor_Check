public class zxing_0056 {

      static void embedDataBits(BitArray dataBits, int maskPattern, ByteMatrix matrix)
          throws WriterException {
        int bitIndex = 0;
        int direction = -1;
        int x = matrix.getWidth() - 1;
        int y = matrix.getHeight() - 1;
        while (x > 0) {
          if (x == 6) {
            x -= 1;
          }
          while (y >= 0 && y < matrix.getHeight()) {
            for (int i = 0; i < 2; ++i) {
              int xx = x - i;
              if (!isEmpty(matrix.get(xx, y))) {
                continue;
              }
              boolean bit = getMaskedBit(dataBits, bitIndex, maskPattern, xx, y);
              if (bitIndex < dataBits.getSize()) {
                ++bitIndex;
              }
              matrix.set(xx, y, bit);
            }
            y += direction;
          }
          direction = -direction;
          y += direction;
          x -= 2;
        }
        if (bitIndex != dataBits.getSize()) {
          throw new WriterException("Not all bits consumed: " + bitIndex + '/' + dataBits.getSize());
        }
      }

      private static boolean getMaskedBit(BitArray dataBits, int bitIndex, int maskPattern, int x, int y) {
        boolean bit = bitIndex < dataBits.getSize() ? dataBits.get(bitIndex) : false;
        if (maskPattern != -1 && MaskUtil.getDataMaskBit(maskPattern, x, y)) {
          return !bit;
        }
        return bit;
      }
}
