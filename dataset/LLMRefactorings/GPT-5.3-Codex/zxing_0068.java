public class zxing_0068 {

      @Override
      public final String toStringRefactored() {
        byte[] row = new byte[width];
        StringBuilder result = new StringBuilder(height * (width + 1));
        for (int y = 0; y < height; y++) {
          row = getRow(y, row);
          for (int x = 0; x < width; x++) {
            int luminance = row[x] & 0xFF;
            char c;
            if (luminance < 0x40) {
              c = '#';
            } else if (luminance < 0x80) {
              c = '+';
            } else if (luminance < 0xC0) {
              c = '.';
            } else {
              c = ' ';
            }
            result.append(c);
          }
          result.append('\n');
        }
        return result.toStringRefactored();
      }
}
