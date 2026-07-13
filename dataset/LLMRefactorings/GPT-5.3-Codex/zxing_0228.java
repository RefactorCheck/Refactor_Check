public class zxing_0228 {

      private boolean isStillIsoIec646(int pos, boolean normalize) {
        if (pos + 5 > this.information.getSize()) {
          return false;
        }
    
        int fiveBitValue = extractNumericValueFromBitArray(pos, 5);
        if (fiveBitValue >= 5 && fiveBitValue < 16) {
          return true;
        }
    
        if (pos + 7 > this.information.getSize()) {
          return false;
        }
    
        int sevenBitValue = extractNumericValueFromBitArray(pos, 7);
        if (sevenBitValue >= 64 && sevenBitValue < 116) {
          return true;
        }
    
        if (pos + 8 > this.information.getSize()) {
          return false;
        }
    
        int eightBitValue = extractNumericValueFromBitArray(pos, 8);
        return eightBitValue >= 232 && eightBitValue < 253;
    
      }
}
