public class zxing_0228 {

      private static final int FIVE_BIT_LENGTH = 5;
      private static final int FIVE_BIT_MIN = 5;
      private static final int FIVE_BIT_MAX = 16;
      private static final int SEVEN_BIT_LENGTH = 7;
      private static final int SEVEN_BIT_MIN = 64;
      private static final int SEVEN_BIT_MAX = 116;
      private static final int EIGHT_BIT_LENGTH = 8;
      private static final int EIGHT_BIT_MIN = 232;
      private static final int EIGHT_BIT_MAX = 253;

      private boolean isStillIsoIec646(int pos) {
        if (pos + FIVE_BIT_LENGTH > this.information.getSize()) {
          return false;
        }
    
        int fiveBitValue = extractNumericValueFromBitArray(pos, FIVE_BIT_LENGTH);
        if (fiveBitValue >= FIVE_BIT_MIN && fiveBitValue < FIVE_BIT_MAX) {
          return true;
        }
    
        if (pos + SEVEN_BIT_LENGTH > this.information.getSize()) {
          return false;
        }
    
        int sevenBitValue = extractNumericValueFromBitArray(pos, SEVEN_BIT_LENGTH);
        if (sevenBitValue >= SEVEN_BIT_MIN && sevenBitValue < SEVEN_BIT_MAX) {
          return true;
        }
    
        if (pos + EIGHT_BIT_LENGTH > this.information.getSize()) {
          return false;
        }
    
        int eightBitValue = extractNumericValueFromBitArray(pos, EIGHT_BIT_LENGTH);
        return eightBitValue >= EIGHT_BIT_MIN && eightBitValue < EIGHT_BIT_MAX;
    
      }
}
