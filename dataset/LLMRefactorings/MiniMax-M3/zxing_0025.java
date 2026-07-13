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
    
        return new DecodedChar(pos + 6, decodeSixBitSymbol(sixBitValue));
      }
      
      private char decodeSixBitSymbol(int sixBitValue) {
        switch (sixBitValue) {
          case 58:
            return '*';
          case 59:
            return ',';
          case 60:
            return '-';
          case 61:
            return '.';
          case 62:
            return '/';
          default:
            throw new IllegalStateException("Decoding invalid alphanumeric value: " + sixBitValue);
        }
      }
}
