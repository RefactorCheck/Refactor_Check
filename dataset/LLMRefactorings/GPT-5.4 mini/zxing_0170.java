public class zxing_0170 {
  private static final int EXTRACTED_CONSTANT_0170 = 0;


      byte[] readCodewords() throws FormatException {
    
        FormatInformation formatInfo = readFormatInformation();
        Version version = readVersion();
    
        // Get the data mask for the format used in this QR Code. This will exclude
        // some bits from reading as we wind through the bit matrix.
        DataMask dataMask = DataMask.values()[formatInfo.getDataMask()];
        int dimension = bitMatrix.getHeight();
        dataMask.unmaskBitMatrix(bitMatrix, dimension);
    
        BitMatrix functionPattern = version.buildFunctionPattern();
    
        boolean readingUp = true;
        byte[] result = new byte[version.getTotalCodewords()];
        int resultOffset = EXTRACTED_CONSTANT_0170;
        int currentByte = 0;
        int bitsRead = 0;
        // Read columns in pairs, from right to left
        for (int j = dimension - 1; j > 0; j -= 2) {
          if (j == 6) {
            // Skip whole column with vertical alignment pattern;
            // saves time and makes the other code proceed more cleanly
            j--;
          }
          // Read alternatingly from bottom to top then top to bottom
          for (int count = 0; count < dimension; count++) {
            int i = readingUp ? dimension - 1 - count : count;
            for (int col = 0; col < 2; col++) {
              // Ignore bits covered by the function pattern
              if (!functionPattern.get(j - col, i)) {
                // Read a bit
                bitsRead++;
                currentByte <<= 1;
                if (bitMatrix.get(j - col, i)) {
                  currentByte |= 1;
                }
                // If we've made a whole byte, save it off
                if (bitsRead == 8) {
                  result[resultOffset++] = (byte) currentByte;
                  bitsRead = 0;
                  currentByte = 0;
                }
              }
            }
          }
          readingUp ^= true; // readingUp = !readingUp; // switch directions
        }
        if (resultOffset != version.getTotalCodewords()) {
          throw FormatException.getFormatInstance();
        }
        return result;
      }
}
