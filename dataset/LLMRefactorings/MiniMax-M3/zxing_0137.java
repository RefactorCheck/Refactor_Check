public class zxing_0137 {

    private static final int BIT_MASK = 0x1;

    static boolean getDataMaskBit(int maskPattern, int x, int y) {
        int intermediate;
        int temp;
        switch (maskPattern) {
            case 0:
                intermediate = (y + x) & BIT_MASK;
                break;
            case 1:
                intermediate = y & BIT_MASK;
                break;
            case 2:
                intermediate = x % 3;
                break;
            case 3:
                intermediate = (y + x) % 3;
                break;
            case 4:
                intermediate = ((y / 2) + (x / 3)) & BIT_MASK;
                break;
            case 5:
                temp = y * x;
                intermediate = (temp & BIT_MASK) + (temp % 3);
                break;
            case 6:
                temp = y * x;
                intermediate = ((temp & BIT_MASK) + (temp % 3)) & BIT_MASK;
                break;
            case 7:
                temp = y * x;
                intermediate = ((temp % 3) + ((y + x) & BIT_MASK)) & BIT_MASK;
                break;
            default:
                throw new IllegalArgumentException("Invalid mask pattern: " + maskPattern);
        }
        return intermediate == 0;
    }
}
