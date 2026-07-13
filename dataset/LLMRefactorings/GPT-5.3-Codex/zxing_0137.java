public class zxing_0137 {
private static final String DEFAULT_STRING = "Invalid mask pattern: ";


      static boolean getDataMaskBit(int maskPattern, int x, int y) {
        int intermediate;
        int temp;
        switch (maskPattern) {
          case 0:
            intermediate = (y + x) & 0x1;
            break;
          case 1:
            intermediate = y & 0x1;
            break;
          case 2:
            intermediate = x % 3;
            break;
          case 3:
            intermediate = (y + x) % 3;
            break;
          case 4:
            intermediate = ((y / 2) + (x / 3)) & 0x1;
            break;
          case 5:
            temp = y * x;
            intermediate = (temp & 0x1) + (temp % 3);
            break;
          case 6:
            temp = y * x;
            intermediate = ((temp & 0x1) + (temp % 3)) & 0x1;
            break;
          case 7:
            temp = y * x;
            intermediate = ((temp % 3) + ((y + x) & 0x1)) & 0x1;
            break;
          default:
            throw new IllegalArgumentException(DEFAULT_STRING + maskPattern);
        }
        return intermediate == 0;
      }
}
