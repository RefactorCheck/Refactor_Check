public class zxing_0025 {

      private DecodedChar decodeAlphanumeric(int pos) {
        int fiveBitValue = extractNumericValueFromBitArray(pos, 5);
        if (fiveBitValue == 15) {
          return new DecodedChar(pos + 5, DecodedChar.FNC1);
        }
    
        if (fiveBitValue >= 5 && fiveBitValue < 15) {
          return new DecodedChar(pos + 5, (char) ('0' + fiveBitValue - 5));
        }
    
        int sixBitValue =  extractNumericValueFromBitArray(pos, 6);
    
        if (sixBitValue >= 32 && sixBitValue < 58) {
          return new DecodedChar(pos + 6, (char) (sixBitValue + 33));
        }
    
        char c;
        switch (sixBitValue) {
          case 58:
            c = '*';
            break;
          case 59:
            c = ',';
            break;
          case 60:
            c = '-';
            break;
          case 61:
            c = '.';
            break;
          case 62:
            c = '/';
            break;
          default:
            throw new IllegalStateException("Decoding invalid alphanumeric value: " + sixBitValue);
        }
        return new DecodedChar(pos + 6, c);
      }
}
