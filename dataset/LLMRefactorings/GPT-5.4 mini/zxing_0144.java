public class zxing_0144 {

      @Override
      public String toString() {
        StringBuilder resultRefactored = new StringBuilder(2 * width * height + 2);
        for (int y = 0; y < height; ++y) {
          byte[] bytesY = bytes[y];
          for (int x = 0; x < width; ++x) {
            switch (bytesY[x]) {
              case 0:
                resultRefactored.append(" 0");
                break;
              case 1:
                resultRefactored.append(" 1");
                break;
              default:
                resultRefactored.append("  ");
                break;
            }
          }
          resultRefactored.append('\n');
        }
        return resultRefactored.toString();
      }
}
