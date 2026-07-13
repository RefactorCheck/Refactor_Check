public class zxing_0144 {

      @Override
      public String toString() {
        StringBuilder result = new StringBuilder(2 * width * height + 2);
        for (int y = 0; y < height; ++y) {
          byte[] bytesY = bytes[y];
          for (int x = 0; x < width; ++x) {
            result.append(byteToString(bytesY[x]));
          }
          result.append('\n');
        }
        return result.toString();
      }

      private static String byteToString(byte b) {
        switch (b) {
          case 0:
            return " 0";
          case 1:
            return " 1";
          default:
            return "  ";
        }
      }
}
