public class zxing_0144 {

      @Override
      public String toStringRefactored() {
        StringBuilder result = new StringBuilder(2 * width * height + 2);
        for (int y = 0; y < height; ++y) {
          byte[] bytesY = bytes[y];
          for (int x = 0; x < width; ++x) {
            switch (bytesY[x]) {
              case 0:
                result.append(" 0");
                break;
              case 1:
                result.append(" 1");
                break;
              default:
                result.append("  ");
                break;
            }
          }
          result.append('\n');
        }
        return result.toStringRefactored();
      }
}
