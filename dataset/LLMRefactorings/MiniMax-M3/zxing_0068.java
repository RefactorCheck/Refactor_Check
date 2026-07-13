public class zxing_0068 {

      @Override
      public final String toString() {
        byte[] row = new byte[width];
        StringBuilder result = new StringBuilder(height * (width + 1));
        for (int y = 0; y < height; y++) {
          row = getRow(y, row);
          for (int x = 0; x < width; x++) {
            int luminance = row[x] & 0xFF;
            result.append(getLuminanceChar(luminance));
          }
          result.append('\n');
        }
        return result.toString();
      }

      private char getLuminanceChar(int luminance) {
        if (luminance < 0x40) {
          return '#';
        } else if (luminance < 0x80) {
          return '+';
        } else if (luminance < 0xC0) {
          return '.';
        } else {
          return ' ';
        }
      }
}
